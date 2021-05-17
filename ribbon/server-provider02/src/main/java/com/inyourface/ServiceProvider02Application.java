package com.inyourface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceProvider02Application {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProvider02Application.class, args);
    }
}
