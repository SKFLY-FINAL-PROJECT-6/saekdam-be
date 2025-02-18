package com.example.demo.domain.comment;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

public interface CommentController {
    ResponseEntity<Comment> create(Jwt jwt, Comment comment);

    ResponseEntity<String> updateContent(String id, String content);

    ResponseEntity<Void> delete(Jwt jwt, String id);
}

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Override
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

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<String> updateContent(
            @PathVariable String id,
            @RequestBody String content) {
        return ResponseEntity.ok(commentService.updateContent(id, content));
    }

    @Override
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