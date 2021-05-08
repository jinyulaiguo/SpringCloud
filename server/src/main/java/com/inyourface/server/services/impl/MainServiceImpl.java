package com.inyourface.server.services.impl;

import com.inyourface.server.services.MainService;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {
    @Override
    public String getArgs(String args) {
        return "参数：" + args;
    }
}
