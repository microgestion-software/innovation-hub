package com.microgestion.example.patterns.resilience.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RateLimitingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RateLimitingApiApplication.class, args);
    }
}