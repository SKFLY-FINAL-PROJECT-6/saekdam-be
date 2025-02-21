package com.example.demo.domain.aitask.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Task {
    private final String id;
    private final String imageId;
    private final String theme;
    private final String requirement;
    private final Float x;
    private final Float y;
    private final Float w;
    private final Float h;

    public static Task create(TaskRequest taskRequest) {
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .imageId(UUID.randomUUID().toString())
                .theme(taskRequest.getTheme())
                .requirement(taskRequest.getRequirement())
                .x(taskRequest.getX())
                .y(taskRequest.getY())
                .w(taskRequest.getW())
                .h(taskRequest.getH())
                .build();
    }
}
