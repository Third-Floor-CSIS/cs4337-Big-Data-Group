package com.example.posts.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    private Long postId;
    private Long userId;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean isLiked = true;

    // Constructor for creating a Like with required fields
    public Like(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.isLiked = true;
    }
}
