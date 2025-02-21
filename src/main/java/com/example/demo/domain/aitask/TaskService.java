package com.example.demo.domain.aitask;

import org.springframework.stereotype.Service;

import com.example.demo.domain.aitask.dto.Task;
import com.example.demo.domain.aitask.dto.TaskProgressMessage;
import com.example.demo.domain.aitask.dto.TaskRequest;

import lombok.RequiredArgsConstructor;

public interface TaskService {
    TaskProgressMessage create(TaskRequest taskRequest);
}

@Service
@RequiredArgsConstructor
class TaskServiceImpl implements TaskService {
    private final TaskPublisher taskPublisher;

    @Override
    public TaskProgressMessage create(TaskRequest taskRequest) {
        Task newTask = Task.create(taskRequest);
        taskPublisher.publish(newTask);

        return TaskProgressMessage.create(newTask);
    }
}