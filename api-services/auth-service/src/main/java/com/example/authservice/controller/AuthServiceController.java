package com.example.authservice.controller;
import com.example.authservice.model.User;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation                                                                                                                                                                                                      .RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthServiceController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/user")
    public User user(@RequestParam(value = "user", defaultValue = "I am User Service") String name) {
        return new User(counter.incrementAndGet(), String.format(template, name));
    }
}