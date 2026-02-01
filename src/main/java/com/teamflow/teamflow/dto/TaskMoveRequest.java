package com.teamflow.teamflow.dto;

import com.teamflow.teamflow.model.TaskStatus;

public class TaskMoveRequest {

    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
