package com.example.demo.domain.post;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, String> {
    List<PostImage> findByPostId(String postId);
}
