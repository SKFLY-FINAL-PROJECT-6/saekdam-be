package com.example.demo.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.comment.CommentRepository;

public interface PostService {

    Post create(Post post);

    String updateTitle(String id, String title);

    String updateContent(String id, String content);

    void delete(String id);

    Post findById(String id);

    Page<Post> findAll(Pageable pageable);

    List<Comment> findAllComments(String id);

    List<Post> findByTitleContaining(String title);

    List<Post> findByContentContaining(String content);
}

@Service
@Transactional
class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostServiceImpl(PostRepository postRepository, CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Post create(Post post) {
        return postRepository.save(post);
    }

    @Override
    public String updateTitle(String id, String title) {
        postRepository.updateTitle(id, title);

        return title;
    }

    @Override
    public String updateContent(String id, String content) {
        postRepository.updateContent(id, content);

        return content;
    }

    @Override
    public void delete(String id) {
        java.time.LocalDateTime deleteTime = java.time.LocalDateTime.now();
        Post postToDelete = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        postToDelete.setDeletedAt(deleteTime);
        postRepository.save(postToDelete);

        commentRepository.deleteByPostId(id, deleteTime);
    }

    @Override
    public Post findById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setViews(post.getViews() + 1);
        return postRepository.save(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllComments(String id) {
        return commentRepository.findByPostId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findByTitleContaining(String title) {
        return postRepository.findByTitleContaining(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> findByContentContaining(String content) {
        return postRepository.findByContentContaining(content);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

}
