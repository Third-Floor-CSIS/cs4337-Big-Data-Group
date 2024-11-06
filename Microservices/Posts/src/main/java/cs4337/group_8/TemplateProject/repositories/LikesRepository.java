package com.example.posts.repository;

import com.example.posts.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    long countByPostIdAndIsLikedTrue(Long postId); // Count active likes for fast lookup
}
