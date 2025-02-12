package com.example.demo.domain.post;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.demo.domain.post.dto.PostCreateResponse;
import com.example.demo.domain.post.dto.PostCreateRequest;
import com.example.demo.domain.post.dto.PostResponse;

import lombok.RequiredArgsConstructor;

public interface PostController {
    ResponseEntity<PostResponse> findById(String id, Jwt jwt);

    ResponseEntity<PostCreateResponse> create(PostCreateRequest postWrite, Jwt jwt);

    ResponseEntity<String> updateTitle(String id, String title);

    ResponseEntity<String> updateContent(String id, String content);

    ResponseEntity<String> delete(String id, Jwt jwt);

    ResponseEntity<Page<Post>> findAll(Pageable pageable);

    ResponseEntity<List<Post>> findByTitleContaining(String title);

    ResponseEntity<List<Post>> findByContentContaining(String content);

    ResponseEntity<String> like(String id, Jwt jwt);

    ResponseEntity<String> unlike(String id, Jwt jwt);
}

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
class PostControllerImpl implements PostController {

    private final PostService postService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(postService.findById(id, jwt));
    }

    @Override
    @PostMapping
    public ResponseEntity<PostCreateResponse> create(
            @RequestBody PostCreateRequest postWrite,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(postService.create(postWrite, jwt));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        postService.delete(id, jwt);
        return ResponseEntity.ok("Deleted");
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

    @Override
    @PostMapping("/{id}/likes")
    public ResponseEntity<String> like(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        postService.like(id, jwt);
        return ResponseEntity.ok("Liked");
    }

    @Override
    @DeleteMapping("/{id}/likes")
    public ResponseEntity<String> unlike(
            @PathVariable String id,
            @AuthenticationPrincipal Jwt jwt) {
        postService.unlike(id, jwt);
        return ResponseEntity.ok("Unliked");
    }

}