package com.example.posts.service;

import com.example.posts.entity.Post;
import com.example.posts.exception.PostException;
import com.example.posts.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPost_shouldSavePost() {
        Post post = new Post();
        when(postRepository.save(post)).thenReturn(post);

        Post savedPost = postService.createPost(post);

        assertNotNull(savedPost);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void getPostById_shouldReturnPostWhenFound() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post foundPost = postService.getPostById(postId);

        assertEquals(postId, foundPost.getId());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void getPostById_shouldThrowExceptionWhenNotFound() {
        Long postId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostException.class, () -> postService.getPostById(postId));
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void getPostsByUser_shouldReturnPosts() {
        Long userId = 1L;
        Post post = new Post();
        when(postRepository.findByUserId(userId)).thenReturn(Collections.singletonList(post));

        List<Post> posts = postService.getPostsByUser(userId);

        assertFalse(posts.isEmpty());
        verify(postRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getPostsByUser_shouldThrowExceptionWhenNoPosts() {
        Long userId = 1L;
        when(postRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        assertThrows(PostException.class, () -> postService.getPostsByUser(userId));
        verify(postRepository, times(1)).findByUserId(userId);
    }

    @Test
    void likePost_shouldIncreaseLikesCount() {
        Long postId = 1L;
        Post post = new Post();
        post.setLikesCount(0);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.likePost(postId);

        assertEquals(1, post.getLikesCount());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void unlikePost_shouldDecreaseLikesCount() {
        Long postId = 1L;
        Post post = new Post();
        post.setLikesCount(1);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.unlikePost(postId);

        assertEquals(0, post.getLikesCount());
        verify(postRepository, times(1)).save(post);
    }
}
