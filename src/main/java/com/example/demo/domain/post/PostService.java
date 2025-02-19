package com.example.demo.domain.post;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.example.demo.domain.post.dto.PostCreateRequest;
import com.example.demo.domain.post.dto.PostResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.jwt.Jwt;
import com.example.demo.domain.post.dto.PostCreateResponse;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.comment.CommentRepository;
import com.example.demo.domain.like.PostLikeRepository;
import com.example.demo.domain.like.entity.PostLike;

public interface PostService {
    String findUserIdById(String id);

    PostCreateResponse create(PostCreateRequest postWrite, Jwt jwt);

    String updateTitle(String id, String title);

    String updateContent(String id, String content);

    void delete(String id);

    PostResponse findById(String id, Jwt jwt);

    Page<Post> findAll(Pageable pageable);

    List<Comment> findAllComments(String id);

    List<UUID> findAllImages(String id);

    List<Post> findByTitleContaining(String title);

    List<Post> findByContentContaining(String content);

    void like(String id, Jwt jwt);

    void unlike(String id, Jwt jwt);
}

@Service
@RequiredArgsConstructor
@Transactional
class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    @Transactional(readOnly = true)
    public String findUserIdById(String id) {
        return postRepository.findUserIdById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public PostCreateResponse create(PostCreateRequest postWrite, Jwt jwt) {
        Post post = Post.create(postWrite, jwt);
        Post savedPost = postRepository.save(post);

        return Optional.ofNullable(postWrite.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> {
                    List<PostImage> savedImages = postImageRepository.saveAll(images.stream()
                            .peek(image -> image.setPostId(savedPost.getId()))
                            .collect(Collectors.toList()));

                    // 이미지 저장 후 thumbnail 설정
                    savedPost.setThumbnail(savedImages.get(0).getId());
                    postRepository.save(savedPost);

                    return new PostCreateResponse(
                            savedImages.stream()
                                    .map(image -> UUID.fromString(image.getId()))
                                    .collect(Collectors.toList()));
                })
                .orElseGet(() -> new PostCreateResponse(List.of()));
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
        commentRepository.deleteByPostId(id, deleteTime);
        Post postToDelete = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        postToDelete.setDeletedAt(deleteTime);
        postRepository.save(postToDelete);
    }

    @Override
    public PostResponse findById(String id, Jwt jwt) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        List<Comment> comments = commentRepository.findByPostId(id);
        boolean isLiked = (jwt != null) ? postLikeRepository.existsByPostIdAndUserId(id, jwt.getSubject()) : false;

        List<UUID> images = postImageRepository.findByPostId(id)
                .stream()
                .map(image -> UUID.fromString(image.getId()))
                .collect(Collectors.toList());

        postRepository.incrementViews(id);

        return PostResponse.of(post, isLiked, images, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllComments(String id) {
        return commentRepository.findByPostId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> findAllImages(String id) {
        return postImageRepository.findByPostId(id)
                .stream()
                .map(image -> UUID.fromString(image.getId()))
                .collect(Collectors.toList());
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

    @Override
    public void like(String id, Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        }
        String userId = jwt.getSubject();

        if (postLikeRepository.existsByPostIdAndUserId(id, userId)) {
            return;
        }

        postLikeRepository.save(PostLike.builder()
                .postId(id)
                .userId(userId)
                .build());

        postRepository.incrementLikes(id);
    }

    @Override
    @Transactional
    public void unlike(String id, Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        }
        String userId = jwt.getSubject();

        if (!postLikeRepository.existsByPostIdAndUserId(id, userId)) {
            return;
        }

        postLikeRepository.deleteByPostIdAndUserId(id, userId);
        postRepository.decrementLikes(id);
    }
}
