package com.example.demo.domain.aitask;

import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.demo.domain.aitask.dto.TaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

public interface TaskPublisher {
    void publish(TaskRequest task);
}

@Component
@RequiredArgsConstructor
class TaskPublisherImpl implements TaskPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String QUEUE_KEY = "ai-task-queue";

    @Override
    public void publish(TaskRequest task) {
        try {
            String message = objectMapper.writeValueAsString(task);
            // 레디스 메세지큐에 Task Push
            redisTemplate.opsForList().rightPush(QUEUE_KEY, message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish message", e);
        }
    }
}