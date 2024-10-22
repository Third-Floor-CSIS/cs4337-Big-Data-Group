package com.example.posts.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postId;

    private String caption;
    private String imageUrl;
    private LocalDateTime createdAt;
    private int userId; // to track which user created the post

    private int likesCount = 0; // to track number of likes

    public Post() {

        this.createdAt = LocalDateTime.now();
    }

}