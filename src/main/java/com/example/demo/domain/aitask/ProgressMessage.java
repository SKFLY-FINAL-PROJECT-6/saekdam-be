package com.example.demo.domain.aitask;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProgressMessage {
    private String message;
    private int progress;

    public ProgressMessage(String message, int progress) {
        this.message = message;
        this.progress = progress;
    }
}
