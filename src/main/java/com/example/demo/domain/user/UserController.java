package com.example.demo.domain.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface UserController {
    ResponseEntity<User> create(User user);

    ResponseEntity<String> login(LoginRequest loginRequest);
}

@RestController
@RequestMapping("/users")
class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
    }

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
}