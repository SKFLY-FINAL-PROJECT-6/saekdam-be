package com.example.demo.domain.post;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.demo.domain.post.dto.PostCreateRequest;

import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLRestriction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@SQLRestriction("deleted_at IS NULL")
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title", nullable = false)
    @Size(min = 5, max = 20, message = "제목은 5자 이상 20자 이하여야 합니다.")
    private String title;

    @Column(name = "content", nullable = false)
    @Size(min = 10, max = 500, message = "내용은 10자 이상 500자 이하여야 합니다.")
    private String content;

    @Column(name = "author")
    @Builder.Default
    private String author = null;

    @Column(name = "user_id")
    @Builder.Default
    private String userId = null;

    @Column(name = "likes", nullable = false)
    @Builder.Default
    private int likes = 0;

    @Column(name = "views", nullable = false)
    @Builder.Default
    private int views = 0;

    @Column(name = "num_of_comments", nullable = false)
    @Builder.Default
    private int numOfComments = 0;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    @Builder.Default
    private LocalDateTime deletedAt = null;

    @Column(name = "thumbnail_id")
    @Builder.Default
    private String thumbnail = null;

    public static Post create(PostCreateRequest postWrite, Jwt jwt) {
        Post post = Post.builder()
                .title(postWrite.getTitle())
                .content(postWrite.getContent())
                .build();

        if (jwt != null) {
            post.setUserId(jwt.getSubject());
            post.setAuthor(jwt.getClaim("name"));
        }

        return post;
    }
}