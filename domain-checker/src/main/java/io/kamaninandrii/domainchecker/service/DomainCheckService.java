package io.kamaninandrii.domainchecker.service;


import io.kamaninandrii.domainchecker.whois.WhoisClient;
import org.springframework.stereotype.Service;

@Service
public class DomainCheckService {

    private final WhoisClient whoisClient;

    public DomainCheckService(WhoisClient whoisClient) {
        this.whoisClient = whoisClient;
    }

    public boolean isDomainFree(String domain) {
        return whoisClient.isDomainFree(domain);
    }
}