package com.example.posts.service;

import com.example.posts.exception.PostException;
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
        List<Post> posts = postRepository.findByUserId(userId);
        if (posts.isEmpty()) {
            throw new PostException("No posts found for user ID " + userId);
        }
        return posts;
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }


    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with ID " + postId + " not found"));

        if (post.getUserId().equals(userId)) {
            postRepository.delete(post);
        } else {
            throw new PostException("User ID " + userId + " is not authorized to delete post with ID " + postId);
        }
    }

    public void likePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with ID " + postId + " not found"));

        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    public void unlikePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with ID " + postId + " not found"));

        post.setLikesCount(post.getLikesCount() - 1);
        postRepository.save(post);
    }
}
