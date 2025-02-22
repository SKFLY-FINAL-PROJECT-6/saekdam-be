package com.example.demo.domain.aitask;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.aitask.dto.TaskRequest;
import com.example.demo.domain.aitask.dto.TaskResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/task-id")
    public ResponseEntity<TaskResponse> createTaskId() {
        return ResponseEntity.ok(taskService.createTaskId());
    }

    @PostMapping("/new-task")
    public ResponseEntity<String> submitTask(
            @RequestBody TaskRequest request) {
        taskService.submitTask(request);
        return ResponseEntity.ok("Task submitted");
    }
}