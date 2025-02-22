package com.example.demo.domain.aitask.dto;

import com.example.demo.domain.aitask.enums.TaskProgress;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TaskProgressMessage {
    @JsonProperty
    private String taskId;

    @JsonProperty
    private TaskProgress status;
}
