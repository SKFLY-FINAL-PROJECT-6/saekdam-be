package com.example.demo.domain.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByTitleContaining(String title);

    List<Post> findByContentContaining(String content);

    @Modifying
    @Query("UPDATE Post p SET p.content = :content WHERE p.id = :id")
    void updateContent(@Param("id") String id, @Param("content") String content);

    @Modifying
    @Query("UPDATE Post p SET p.title = :title WHERE p.id = :id")
    void updateTitle(@Param("id") String id, @Param("title") String title);
}
