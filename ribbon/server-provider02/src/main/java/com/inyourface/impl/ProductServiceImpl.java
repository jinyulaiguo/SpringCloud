package com.inyourface.impl;

import com.inyourface.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public String getName() {
        return "name--02";
    }
}
