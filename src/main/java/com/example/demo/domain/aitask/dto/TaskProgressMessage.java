package com.example.demo.domain.aitask.dto;

import com.example.demo.domain.aitask.enums.TaskProgress;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class TaskProgressMessage {
    private String taskId;
    private String imageId;
    private TaskProgress status;

    public static TaskProgressMessage create(Task task) {
        return TaskProgressMessage.builder()
                .taskId(task.getId())
                .imageId(task.getImageId())
                .status(task.getTaskProgress())
                .build();
    }
}
