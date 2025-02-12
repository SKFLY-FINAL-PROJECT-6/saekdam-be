package com.example.demo.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.like.entity.PostLike;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, String> {
        boolean existsByPostIdAndUserId(String postId, String userId);

        void deleteByPostIdAndUserId(String postId, String userId);
}
