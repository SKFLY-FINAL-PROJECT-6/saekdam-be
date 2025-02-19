package com.example.demo.domain.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findByTitleContaining(String title);

    List<Post> findByContentContaining(String content);

    @Query("SELECT p.userId FROM Post p WHERE p.id = :id")
    Optional<String> findUserIdById(String id);

    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
    void incrementViews(@Param("id") String id);

    @Modifying
    @Query("UPDATE Post p SET p.likes = p.likes - 1 WHERE p.id = :id")
    void decrementLikes(@Param("id") String id);

    @Modifying
    @Query("UPDATE Post p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void incrementLikes(@Param("id") String id);

    @Modifying
    @Query("UPDATE Post p SET p.content = :content WHERE p.id = :id")
    void updateContent(@Param("id") String id, @Param("content") String content);

    @Modifying
    @Query("UPDATE Post p SET p.title = :title WHERE p.id = :id")
    void updateTitle(@Param("id") String id, @Param("title") String title);
}
