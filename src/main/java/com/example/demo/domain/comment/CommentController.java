package com.example.demo.domain.comment;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> create(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Comment comment) {
        if (jwt != null) {
            comment.setUserId(jwt.getSubject());
            comment.setAuthor(jwt.getClaim("name"));
        }

        return ResponseEntity.ok(commentService.create(comment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateContent(
            @PathVariable String id,
            @RequestBody String content) {
        return ResponseEntity.ok(commentService.updateContent(id, content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String id) {
        String authorId = commentService.findById(id).getUserId();
        String userId = jwt != null ? jwt.getSubject() : null;

        if (authorId != null && !authorId.equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "작성자만 삭제할 수 있습니다.");
        }

        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}