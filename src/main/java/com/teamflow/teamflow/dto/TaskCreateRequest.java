package com.teamflow.teamflow.dto;

import com.teamflow.teamflow.model.TaskStatus;

import java.util.UUID;

public class TaskCreateRequest {

    private UUID projectId;
    private String title;
    private String description;

    // OPTIONAL
    private TaskStatus status;        // required ONLY if assignedUserId != null
    private UUID assignedUserId;      // nullable = backlog

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UUID getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(UUID assignedUserId) {
        this.assignedUserId = assignedUserId;
    }
}
