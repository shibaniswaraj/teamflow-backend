package com.teamflow.teamflow.dto;

import com.teamflow.teamflow.model.TaskStatus;

import java.util.UUID;

public class TaskUpdateRequest {

    private String title;
    private String description;
    private UUID assignedUserId;
    private TaskStatus status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(UUID assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
}
