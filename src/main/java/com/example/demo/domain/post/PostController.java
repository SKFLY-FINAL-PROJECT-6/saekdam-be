package com.example.demo.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.demo.domain.post.dto.PostCreateResponse;
import com.example.demo.domain.post.dto.PostCreateRequest;
import com.example.demo.domain.post.dto.PostResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(postService.findById(id, jwt));
    }

    @PostMapping
    public ResponseEntity<PostCreateResponse> create(
            @RequestBody PostCreateRequest postWrite,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(postService.create(postWrite, jwt));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {

        String authorId = postService.findUserIdById(id).orElse(null);
        String userId = jwt != null ? jwt.getSubject() : null;

        if (authorId != null && !authorId.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "작성자만 삭제할 수 있습니다.");
        }

        postService.delete(id);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/title/{id}")
    public ResponseEntity<String> updateTitle(
            @PathVariable String id,
            @RequestBody String title) {
        return ResponseEntity.ok(postService.updateTitle(id, title));
    }

    @PutMapping("/content/{id}")
    public ResponseEntity<String> updateContent(
            @PathVariable String id,
            @RequestBody String content) {
        return ResponseEntity.ok(postService.updateContent(id, content));
    }

    @GetMapping
    public ResponseEntity<Page<Post>> findAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(pageable));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Post>> findByTitleContaining(
            @PageableDefault() @RequestParam String title) {
        return ResponseEntity.ok(postService.findByTitleContaining(title));
    }

    @GetMapping("/search/content")
    public ResponseEntity<List<Post>> findByContentContaining(
            @RequestParam String content) {
        return ResponseEntity.ok(postService.findByContentContaining(content));
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<String> like(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        postService.like(id, jwt);
        return ResponseEntity.ok("Liked");
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<String> unlike(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        postService.unlike(id, jwt);
        return ResponseEntity.ok("Unliked");
    }

}