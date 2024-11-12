package cs4337.group_8.TemplateProject.services;

import cs4337.group_8.TemplateProject.entities.Like;
import cs4337.group_8.TemplateProject.entities.Post;
import cs4337.group_8.TemplateProject.exceptions.PostException;
import cs4337.group_8.TemplateProject.repositories.LikesRepository;
import cs4337.group_8.TemplateProject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikesRepository likesRepository;

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

    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with ID " + postId + " not found"));

        Optional<Like> existingLike = likesRepository.findByPostIdAndUserId(postId, userId);

        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            if (!like.isLiked()) {
                like.setLiked(true); // Reactivate like
                likesRepository.save(like);
                post.setLikesCount(post.getLikesCount() + 1); // Increment fast lookup counter
                postRepository.save(post);
            } else {
                throw new PostException("User ID " + userId + " has already liked post with ID " + postId);
            }
        } else {
            // Add a new like entry if none exists
            Like newLike = new Like(postId, userId);
            likesRepository.save(newLike);
            post.setLikesCount(post.getLikesCount() + 1); // Increment fast lookup counter
            postRepository.save(post);
        }
    }

    public void unlikePost(Long postId, Long userId) {
        Optional<Like> likeOptional = likesRepository.findByPostIdAndUserId(postId, userId);

        if (likeOptional.isEmpty()) {
            throw new PostException("Like entry for Post ID " + postId + " by User ID " + userId + " not found");
        }

        Like like = likeOptional.get();

        if (!like.isLiked()) {
            throw new PostException("Post with ID " + postId + " is already unliked by User ID " + userId);
        }

        like.setLiked(false);
        likesRepository.save(like);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with ID " + postId + " not found"));

        // Only decrement if likesCount is greater than 0
        if (post.getLikesCount() > 0) {
            post.setLikesCount(post.getLikesCount() - 1);
        }

        postRepository.save(post);
    }

    public long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post with ID " + postId + " not found"));
        return post.getLikesCount();
    }
}
