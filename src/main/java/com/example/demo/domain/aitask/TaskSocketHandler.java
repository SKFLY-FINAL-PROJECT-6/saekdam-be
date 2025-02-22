package com.example.demo.domain.aitask;

import com.example.demo.domain.aitask.dto.TaskProgressMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

@Component
@RequiredArgsConstructor
public class TaskSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 연결 성립
    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        String taskId = extractTaskId(session);
        sessions.put(taskId, session);
    }

    private String extractTaskId(WebSocketSession session) {
        String path = Optional.ofNullable(session.getUri())
                .map(uri -> uri.getPath())
                .orElseThrow(() -> new IllegalArgumentException("WebSocket URI cannot be null"));

        // /ws/tasks/{taskId}
        String[] pathSegments = path.split("/");
        if (pathSegments.length < 4) {
            throw new IllegalArgumentException("Invalid URI format");
        }
        return pathSegments[3]; // /ws/tasks/{taskId} 에서 마지막 path segment가 taskId
    }

    // 연결 종료
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String taskId = extractTaskId(session);
        sessions.remove(taskId);
    }

    // TaskProgressMessage 객체 전송
    public void sendProgress(TaskProgressMessage message) {
        WebSocketSession session = sessions.get(message.getTaskId());

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}