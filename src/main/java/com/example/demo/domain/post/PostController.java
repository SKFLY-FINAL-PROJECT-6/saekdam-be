package com.example.demo.domain.post;

import java.util.List;

import com.example.demo.domain.post.PostImage;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.HashMap;
import java.util.Map;
import com.example.demo.domain.comment.Comment;
import com.example.demo.domain.post.dto.PostWrite;

public interface PostController {
    ResponseEntity<Map<String, Object>> findById(String id);

    ResponseEntity<Post> create(PostWrite postWrite, Jwt jwt);

    ResponseEntity<String> updateTitle(String id, String title);

    ResponseEntity<String> updateContent(String id, String content);

    ResponseEntity<String> delete(String id, Jwt jwt);

    ResponseEntity<Page<Post>> findAll(Pageable pageable);

    ResponseEntity<List<Post>> findByTitleContaining(String title);

    ResponseEntity<List<Post>> findByContentContaining(String content);
}

@RestController
@RequestMapping("/posts")
class PostControllerImpl implements PostController {

    private final PostService postService;

    public PostControllerImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> findById(
            @PathVariable String id) {
        Post post = postService.findById(id);
        List<Comment> comments = postService.findAllComments(id);

        Map<String, Object> response = new HashMap<>();
        response.put("post", post);
        response.put("comments", comments);

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<Post> create(
            @RequestBody PostWrite postWrite,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(postService.create(postWrite, jwt));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        String author = postService.findById(id).getUserId();
        String userId = jwt != null ? jwt.getSubject() : null;

        if (author != null && !author.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "작성자만 삭제할 수 있습니다.");
        }

        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/title/{id}")
    public ResponseEntity<String> updateTitle(
            @PathVariable String id,
            @RequestBody String title) {
        return ResponseEntity.ok(postService.updateTitle(id, title));
    }

    @Override
    @PutMapping("/content/{id}")
    public ResponseEntity<String> updateContent(
            @PathVariable String id,
            @RequestBody String content) {
        return ResponseEntity.ok(postService.updateContent(id, content));
    }

    @Override
    @GetMapping
    public ResponseEntity<Page<Post>> findAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(pageable));
    }

    @Override
    @GetMapping("/search/title")
    public ResponseEntity<List<Post>> findByTitleContaining(
            @PageableDefault() @RequestParam String title) {
        return ResponseEntity.ok(postService.findByTitleContaining(title));
    }

    @Override
    @GetMapping("/search/content")
    public ResponseEntity<List<Post>> findByContentContaining(
            @RequestParam String content) {
        return ResponseEntity.ok(postService.findByContentContaining(content));
    }
}