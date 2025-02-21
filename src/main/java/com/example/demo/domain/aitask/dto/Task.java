package com.example.demo.domain.aitask.dto;

import java.util.UUID;

import com.example.demo.domain.aitask.enums.TaskProgress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Task {
    private final String id;
    private final String imageId;
    private final TaskProgress taskProgress;
    private final String theme;
    private final String requirement;

    public static Task create(TaskRequest taskRequest) {
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .imageId(UUID.randomUUID().toString())
                .taskProgress(TaskProgress.WAITING)
                .theme(taskRequest.getTheme())
                .requirement(taskRequest.getRequirement())
                .build();
    }
}
