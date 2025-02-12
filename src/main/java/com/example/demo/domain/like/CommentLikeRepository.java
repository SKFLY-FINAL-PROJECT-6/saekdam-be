package com.example.demo.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.domain.like.entity.CommentLike;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, String> {

}
