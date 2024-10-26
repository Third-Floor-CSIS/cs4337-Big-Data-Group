package com.example.postservice.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.postservice.model.Post;

@RestController
public class PostController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/post")
	public Post post(@RequestParam(value = "post", defaultValue = "I am Post Service") String name) {
		return new Post(counter.incrementAndGet(), String.format(template, name));
	}
}