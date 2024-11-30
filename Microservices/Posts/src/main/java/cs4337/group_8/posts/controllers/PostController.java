//package com.example.posts.controllers;
package cs4337.group_8.posts.controllers;

import cs4337.group_8.posts.DTO.PostDTO;
import cs4337.group_8.posts.entities.Post;
import cs4337.group_8.posts.services.JwtService;
import cs4337.group_8.posts.services.PostService;
import cs4337.group_8.posts.mappers.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
//Post Controller
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private PostService postService;
    private final JwtService jwtService;

    public PostController(PostService postService, JwtService jwtService){
        this.postService = postService;
        this.jwtService = jwtService;
    }

    @Autowired
    private PostMapper postMapper;

    @PostMapping
    public PostDTO createPost(
            @RequestBody PostDTO postDTO,
            @RequestHeader(name="Authorization") String jwtToken
    ) {
        Post post = postMapper.toEntity(postDTO);
        return postMapper.toDTO(postService.createPost(post, jwtToken));
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
