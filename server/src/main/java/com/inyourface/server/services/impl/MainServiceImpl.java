package com.inyourface.server.services.impl;

import com.inyourface.server.services.MainService;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {
    @Override
    public String getAgrs(String args) {
        return "参数：" + args;
    }
}
