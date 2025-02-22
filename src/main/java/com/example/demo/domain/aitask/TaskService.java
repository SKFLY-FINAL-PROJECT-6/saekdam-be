package com.example.demo.domain.aitask;

import org.springframework.stereotype.Service;

import com.example.demo.domain.aitask.dto.TaskResponse;
import com.example.demo.domain.aitask.dto.TaskRequest;

import lombok.RequiredArgsConstructor;
import java.util.UUID;

public interface TaskService {
    TaskResponse createTaskId();

    void submitTask(TaskRequest request);
}

@Service
@RequiredArgsConstructor
class TaskServiceImpl implements TaskService {
    private final TaskPublisher taskPublisher;

    @Override
    public TaskResponse createTaskId() {
        return new TaskResponse(UUID.randomUUID().toString());
    }

    @Override
    public void submitTask(TaskRequest request) {
        taskPublisher.publish(request);
    }
}