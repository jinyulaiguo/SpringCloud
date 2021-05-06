package com.inyourface.web.controller;

import com.inyourface.server.apis.MainServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainWebController {
    @Autowired
    private MainServiceApi mainServiceApi;

    @Value("${aa.bb}")
    String value;

    @RequestMapping("/value")
    public String value() {
        return "value:" + value;
    }


    @RequestMapping("/getName")
    public String getName() {
        return mainServiceApi.getArgs("aaa");
    }
}
