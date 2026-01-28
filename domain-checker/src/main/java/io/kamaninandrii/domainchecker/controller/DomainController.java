package io.kamaninandrii.domainchecker.controller;

import io.kamaninandrii.domainchecker.service.DomainCheckService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class DomainController {

    private final DomainCheckService service;

    public DomainController(DomainCheckService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/check-file")
    public String checkFile(@RequestParam("file") MultipartFile file, Model model) throws Exception {

        Map<String, Boolean> results = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String domain;
            while ((domain = reader.readLine()) != null) {
                if (domain.isBlank()) continue;

                boolean free = service.isDomainFree(domain.trim());
                results.put(domain.trim(), free);
            }
        }

        model.addAttribute("results", results);
        return "index";
    }
}