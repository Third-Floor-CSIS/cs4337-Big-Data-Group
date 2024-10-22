package com.example.posts.service;

import com.example.posts.entity.Post;
import com.example.posts.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getPostsByUser(Long userId) {
        return postRepository.findByUserId(userId);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public void deletePost(Long postId, Long userId) {
        postRepository.findById(postId).ifPresent(post -> {
            if (post.getUserId().equals(userId)) {
                postRepository.delete(post);
            }
        });
    }

    public void likePost(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
        });
    }

    public void unlikePost(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            post.setLikesCount(post.getLikesCount() - 1);
            postRepository.save(post);
        });
    }
}