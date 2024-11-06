package com.example.posts.mapper;

import com.example.posts.dto.PostDTO;
import com.example.posts.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    // Converts Post to PostDTO
    public PostDTO toDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setPicUrl(post.getPicUrl());
        dto.setLikesCountDTO(post.getLikesCount());
        return dto;
    }

    // Converts PostDTO to Post
    public Post toEntity(PostDTO dto) {
        Post post = new Post();
        post.setUserId(dto.getUserId());
        post.setPicUrl(dto.getPicUrl());
        post.setId(dto.getId());
        post.setLikesCount(dto.getLikesCountDTO());
        return post;
    }
}
