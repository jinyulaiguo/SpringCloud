package com.inyourface.server.apis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "server-service", contextId = "MainServiceApi")
public interface MainServiceApi {
    @RequestMapping("/main/getArgs")
    public String getArgs(String name);
}
