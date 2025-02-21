package com.example.demo.domain.aitask;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.example.demo.domain.aitask.dto.TaskProgressMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TaskSubscriber implements MessageListener {
    private final RedisMessageListenerContainer redisMessageListener;
    private final ObjectMapper objectMapper;
    private final TaskSocketHandler taskSocketHandler;

    private static final String CHANNEL = "ai-task-channel";

    @PostConstruct
    private void init() {
        redisMessageListener.addMessageListener(this, new ChannelTopic(CHANNEL));
    }

    @Override
    public void onMessage(@NonNull Message message, @Nullable byte[] pattern) {
        try {
            String messageBody = new String(message.getBody());
            TaskProgressMessage progressMessage = objectMapper.readValue(messageBody, TaskProgressMessage.class);

            processMessage(progressMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMessage(TaskProgressMessage progressMessage) {
        taskSocketHandler.sendProgress(progressMessage);
    }
}