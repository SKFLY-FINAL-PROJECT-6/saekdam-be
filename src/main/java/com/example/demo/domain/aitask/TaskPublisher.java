package com.example.demo.domain.aitask;

import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.demo.domain.aitask.dto.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

public interface TaskPublisher {
    void publish(Task task);
}

@Component
@RequiredArgsConstructor
class TaskPublisherImpl implements TaskPublisher {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHANNEL = "ai-task-channel";
    private static final String QUEUE_KEY = "ai-task-queue";

    @Override
    public void publish(Task task) {
        try {
            String message = objectMapper.writeValueAsString(task);
            // 레디스 메세지큐에 Task Push
            redisTemplate.opsForList().rightPush(QUEUE_KEY, message);
            // 레디스 채널에 Task Id Publish
            redisTemplate.convertAndSend(CHANNEL, "Task Loaded: " + task.getId());
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish message", e);
        }
    }
}