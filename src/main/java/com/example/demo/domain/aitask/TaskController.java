package com.example.demo.domain.aitask;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.aitask.dto.TaskProgressMessage;
import com.example.demo.domain.aitask.dto.TaskRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskProgressMessage> create(TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.create(taskRequest));
    }
}
