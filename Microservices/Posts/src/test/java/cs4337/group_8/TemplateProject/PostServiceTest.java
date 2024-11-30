package cs4337.group_8.TemplateProject.services;

import cs4337.group_8.posts.entities.Like;
import cs4337.group_8.posts.entities.Post;
import cs4337.group_8.posts.exceptions.PostException;
import cs4337.group_8.posts.repositories.LikesRepository;
import cs4337.group_8.posts.repositories.PostRepository;
import cs4337.group_8.posts.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private LikesRepository likesRepository;

    @InjectMocks
    private PostService postService;

    private Post post;
    private Long postId = 1L;
    private Long userId = 2L;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(postId);
        post.setUserId(userId);
        post.setLikesCount(0);
    }

    @Test
    void likePost_shouldAddLikeAndIncrementCount() {
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likesRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        postService.likePost(postId, userId);

        assertEquals(1, post.getLikesCount());
        verify(likesRepository, times(1)).save(any(Like.class));
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void unlikePost_shouldToggleLikeAndDecrementCount() {
        Like like = new Like(postId, userId);
        like.setLiked(true);// Explicitly set to liked initially
        System.out.println("Like status: " + like.isLiked());
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likesRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(like));

        postService.unlikePost(postId, userId);

        assertFalse(like.isLiked());
        assertEquals(0, post.getLikesCount());
        verify(likesRepository, times(1)).save(like);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void getLikeCount_shouldReturnActiveLikesCount() {
        post.setLikesCount(5); // Set the likesCount directly on the post for fast lookup
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        long likeCount = postService.getLikeCount(postId);

        assertEquals(5, likeCount);
        verify(postRepository, times(1)).findById(postId);
    }


    @Test
    void unlikePost_shouldThrowExceptionIfNotLiked() {
        when(likesRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        assertThrows(PostException.class, () -> postService.unlikePost(postId, userId));
    }

    //Trys to duplicate likes for same user
    @Test
    void likePost_shouldThrowExceptionIfAlreadyLiked() {
        // Arrange
        Like like = new Like(postId, userId);
        like.setLiked(true); // Simulate that the user already liked the post
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likesRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.of(like));

        // Act & Assert
        PostException exception = assertThrows(PostException.class, () -> postService.likePost(postId, userId));
        assertEquals("User ID " + userId + " has already liked post with ID " + postId, exception.getMessage());

        // Verify that likesCount was not incremented and the like wasn't saved again
        assertEquals(0, post.getLikesCount()); // Expecting no change in likesCount
        verify(likesRepository, never()).save(any(Like.class));
        verify(postRepository, never()).save(post);
    }

    @Test
    void unlikePost_shouldThrowExceptionIfNotPreviouslyLiked() {
        // Arrange
        when(likesRepository.findByPostIdAndUserId(postId, userId)).thenReturn(Optional.empty());

        // Act & Assert
        PostException exception = assertThrows(PostException.class, () -> postService.unlikePost(postId, userId));
        assertEquals("Like entry for Post ID " + postId + " by User ID " + userId + " not found", exception.getMessage());

        // Verify that likesCount remains unchanged and unlike wasn't processed
        assertEquals(0, post.getLikesCount()); // Expecting no decrement in likesCount
        verify(likesRepository, never()).save(any(Like.class));
        verify(postRepository, never()).save(post);
    }



}
