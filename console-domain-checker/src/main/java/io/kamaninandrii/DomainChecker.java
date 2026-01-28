package io.kamaninandrii;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DomainChecker {


    private static final Map<String, String> tldCache = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        Path filePath = Path.of("domains.txt");
        List<String> domains = Files.readAllLines(filePath);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (String domain : domains) {
            if (domain.isBlank()) continue;

            executor.submit(() -> {
                try {
                    boolean free = checkDomainWithRetry(domain.trim(), 2);
                    System.out.println(domain + " : " + (free ? "FREE" : "TAKEN"));
                } catch (Exception e) {
                    System.out.println(domain + " : ERROR");
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(15, TimeUnit.MINUTES);
    }


    private static boolean checkDomainWithRetry(String domain, int attempts) {
        for (int i = 0; i < attempts; i++) {
            try {
                return isDomainFree(domain);
            } catch (Exception ignored) {
                try { Thread.sleep(500); } catch (InterruptedException ignored2) {}
            }
        }
        return false;
    }

    static boolean isDomainFree(String domain) throws IOException {
        String whoisServer = getWhoisServer(domain);
        if (whoisServer == null) return true;

        String response = queryWhois(whoisServer, domain).toLowerCase();

        return response.contains("no match")
                || response.contains("not found")
                || response.contains("no entries found");
    }

    static String getWhoisServer(String domain) throws IOException {
        String tld = domain.substring(domain.lastIndexOf('.') + 1).toLowerCase();

        // проверяем кеш
        if (tldCache.containsKey(tld)) {
            return tldCache.get(tld);
        }

        String response = queryWhois("whois.iana.org", tld).toLowerCase();
        for (String line : response.split("\n")) {
            if (line.startsWith("whois:")) {
                String server = line.split(":")[1].trim();
                tldCache.put(tld, server); // сохраняем в кеш
                return server;
            }
        }
        return null;
    }

    static String queryWhois(String server, String query) throws IOException {
        StringBuilder response = new StringBuilder();


        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(server, 43), 5000);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.write(query + "\r\n");
            out.flush();

            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append("\n");
            }
        }
        return response.toString();
    }
}