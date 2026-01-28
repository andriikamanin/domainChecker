package io.kamaninandrii.domainchecker.whois;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WhoisClient {

    private final Map<String, String> tldCache = new ConcurrentHashMap<>();

    public boolean isDomainFree(String domain) {
        try {
            String whoisServer = getWhoisServer(domain);
            if (whoisServer == null) return true;

            String response = queryWhois(whoisServer, domain).toLowerCase();

            return response.contains("no match")
                    || response.contains("not found")
                    || response.contains("no entries found");

        } catch (Exception e) {
            return true;
        }
    }

    private String getWhoisServer(String domain) throws IOException {
        String tld = domain.substring(domain.lastIndexOf('.') + 1).toLowerCase();

        if (tldCache.containsKey(tld)) {
            return tldCache.get(tld);
        }

        String response = queryWhois("whois.iana.org", tld).toLowerCase();
        for (String line : response.split("\n")) {
            if (line.startsWith("whois:")) {
                String server = line.split(":")[1].trim();
                tldCache.put(tld, server);
                return server;
            }
        }
        return null;
    }

    private String queryWhois(String server, String query) throws IOException {
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