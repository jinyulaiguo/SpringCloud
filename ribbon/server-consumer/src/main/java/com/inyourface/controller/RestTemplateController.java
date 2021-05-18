package com.inyourface.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/template")
public class RestTemplateController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getName")
    public String getName() {
        String url = "http://service-provider/product/getName";
        return restTemplate.getForObject(url, String.class);
    }
}
