package com.example.demo.domain.aitask.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TaskRequest {
    private String taskId;
    private String theme;
    private String requirement;
    private Float x;
    private Float y;
    private Float w;
    private Float h;
}
