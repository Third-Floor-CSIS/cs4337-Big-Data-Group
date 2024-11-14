//package com.example.posts.controllers;
package cs4337.group_8.TemplateProject.controllers;

import cs4337.group_8.TemplateProject.DTO.PostDTO;
import cs4337.group_8.TemplateProject.entities.Post;
import cs4337.group_8.TemplateProject.services.PostService;
import cs4337.group_8.TemplateProject.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
//Post Controller
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    @PostMapping
    public PostDTO createPost(@RequestBody PostDTO postDTO) {
        Post post = postMapper.toEntity(postDTO);
        return postMapper.toDTO(postService.createPost(post));
    }

    @GetMapping("/user/{userId}")
    public List<PostDTO> getUserPosts(@PathVariable Long userId) {
        return postService.getPostsByUser(userId).stream()
                .map(postMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId)
                .map(post -> ResponseEntity.ok(postMapper.toDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }
}
