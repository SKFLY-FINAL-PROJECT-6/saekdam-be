package com.example.demo.domain.comment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.post.Post;
import com.example.demo.domain.post.PostRepository;

public interface CommentService {
    Comment findById(String id);

    Comment create(Comment comment);

    void delete(String id);

    String updateContent(String id, String content);

    List<Comment> findByPostId(String postId);
}

@Service
@Transactional
class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Comment findById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    @Override
    public Comment create(Comment comment) {
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setNumOfComments(post.getNumOfComments() + 1);
        postRepository.save(post);

        return commentRepository.save(comment);
    }

    @Override
    public String updateContent(String id, String content) {
        commentRepository.updateContent(id, content);

        return content;
    }

    @Override
    public void delete(String id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setDeletedAt(java.time.LocalDateTime.now());

        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setNumOfComments(post.getNumOfComments() - 1);
        postRepository.save(post);

        commentRepository.save(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByPostId(String postId) {
        return commentRepository.findByPostId(postId);
    }
}