package com.example.demo.domain.user;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.user.dto.LoginRequest;

import lombok.RequiredArgsConstructor;

public interface UserController {

    ResponseEntity<User> create(User user);

    ResponseEntity<String> login(LoginRequest loginRequest);

    ResponseEntity<User> findById(Jwt jwt);
}

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<User> findById(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(userService.findById(jwt));
    }
}