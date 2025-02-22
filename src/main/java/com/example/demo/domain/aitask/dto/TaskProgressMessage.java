package com.example.demo.domain.aitask.dto;

import com.example.demo.domain.aitask.enums.TaskProgress;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TaskProgressMessage {
    private String taskId;
    private TaskProgress status;
}
