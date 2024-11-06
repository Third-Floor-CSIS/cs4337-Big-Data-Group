package com.example.posts.dto;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private Long userId;
    private String picUrl;
    private int likesCountDTO;



}
