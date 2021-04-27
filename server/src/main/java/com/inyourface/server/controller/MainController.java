package com.inyourface.server.controller;

import com.inyourface.server.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {
    @Autowired
    private MainService mainService;

    @RequestMapping("/getArgs")
    public String get(String args) {
        return mainService.getAgrs(args);
    }
}
