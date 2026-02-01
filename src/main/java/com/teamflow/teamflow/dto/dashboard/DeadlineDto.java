package com.teamflow.teamflow.dto.dashboard;

import java.time.LocalDate;
import java.util.UUID;

public class DeadlineDto {

    private UUID taskId;
    private String taskTitle;
    private LocalDate deadline;

    public DeadlineDto(
            UUID taskId,
            String taskTitle,
            LocalDate deadline
    ) {
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.deadline = deadline;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
}
