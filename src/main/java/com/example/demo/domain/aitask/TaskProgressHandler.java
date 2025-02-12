package com.example.demo.domain.aitask;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

@Component
@RequiredArgsConstructor
public class TaskProgressHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 클라이언트 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String taskId = extractTaskId(session); // URL 파라미터에서 taskId 추출
        sessions.put(taskId, session);
    }

    private String extractTaskId(WebSocketSession session) {
        // URL 파라미터에서 taskId를 추출하는 로직을 구현합니다.
        // 예시로, session.getUri().getQuery()를 사용하여 taskId를 추출할 수 있습니다.
        // 실제 구현은 필요에 따라 수정해야 합니다.
        String query = session.getUri().getQuery();
        // query에서 taskId를 추출하는 로직을 추가합니다.
        return "exampleTaskId"; // 실제 taskId를 반환하도록 수정합니다.
    }

    // 연결 종료 시
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String taskId = extractTaskId(session);
        sessions.remove(taskId);
    }

    // Progress 메시지 전송
    public void sendProgress(String taskId, int progress, String status) {
        WebSocketSession session = sessions.get(taskId);
        if (session != null && session.isOpen()) {
            ProgressMessage message = new ProgressMessage(status, progress);
            try {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}