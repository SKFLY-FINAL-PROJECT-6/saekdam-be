package com.example.demo.domain.comment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findByPostId(String postId);

    @Modifying
    @Query("UPDATE Comment c SET c.content = :content WHERE c.id = :id")
    void updateContent(@Param("id") String id, @Param("content") String content);

    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = :deletedAt WHERE c.postId = :postId")
    void deleteByPostId(@Param("postId") String postId, @Param("deletedAt") LocalDateTime deletedAt);
}
