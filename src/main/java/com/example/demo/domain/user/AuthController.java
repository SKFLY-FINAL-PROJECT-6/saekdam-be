package com.example.demo.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/kakao/login")
    public ResponseEntity<Map<String, String>> getKakaoLoginUrl() {
        String loginUrl = authService.generateKakaoLoginUrl();
        return ResponseEntity.ok(Map.of("loginUrl", loginUrl));
    }

    @GetMapping("/oauth2/kakao/callback")
    public ResponseEntity<String> callback(
            @RequestParam String code) {
        return ResponseEntity.ok(authService.processKakaoCallback(code));
    }
}
