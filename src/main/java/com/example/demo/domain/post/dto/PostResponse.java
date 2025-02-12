package com.example.demo.domain.post.dto;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.post.Post;

import lombok.Builder;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class PostResponse {
    private String id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private int numOfComments;
    private int views;
    private int likes;
    private boolean isLiked;
    private List<UUID> imageIds;
    private List<Comment> comments;

    public static PostResponse of(Post post, boolean isLiked, List<UUID> images, List<Comment> comments) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getAuthor())
                .createdAt(post.getCreatedAt())
                .numOfComments(post.getNumOfComments())
                .views(post.getViews())
                .likes(post.getLikes())
                .isLiked(isLiked)
                .imageIds(images)
                .comments(comments)
                .build();
    }
}
