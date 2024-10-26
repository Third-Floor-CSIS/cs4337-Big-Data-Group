package com.example.profileservice.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.profileservice.model.Profile;

@RestController
public class ProfileController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/profile")
	public Profile profile(@RequestParam(value = "profile", defaultValue = "I am Profile Service") String name) {
		return new Profile(counter.incrementAndGet(), String.format(template, name));
	}
}