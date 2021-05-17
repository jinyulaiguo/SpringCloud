package com.inyourface.impl;

import com.inyourface.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Override
    public String getName() {
        ServiceInstance instance = loadBalancerClient.choose("service-provider");
        URI uri = instance.getUri();
        System.out.println(instance.getPort());
        System.out.println(uri.toASCIIString());

        return "name";
    }
}
