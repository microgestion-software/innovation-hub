package com.microgestion.example.hub.rewrite.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Hola desde Spring Boot 2.7.0 con Java 17!";
    }
}