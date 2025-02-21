package com.example.demo.config;

import com.example.demo.domain.aitask.TaskSocketHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final TaskSocketHandler taskSocketHandler;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(taskSocketHandler, "/ws/tasks/{taskId}")
                .setAllowedOrigins("*"); // CORS 설정. 추후 필요하면 변경
    }
}