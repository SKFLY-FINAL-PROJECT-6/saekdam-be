package com.example.demo.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String email;

    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하여야 합니다.")
    private String password;
}
