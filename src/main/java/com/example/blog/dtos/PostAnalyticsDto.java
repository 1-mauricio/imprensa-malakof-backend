package com.example.blog.dtos;


public class PostAnalyticsDto {
    private Long postId;
    private Long viewCount;

    public PostAnalyticsDto(Long postId, Long viewCount) {
        this.postId = postId;
        this.viewCount = viewCount;
    }

    // Getters e Setters
}
