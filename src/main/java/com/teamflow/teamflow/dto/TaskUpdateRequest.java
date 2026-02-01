package com.teamflow.teamflow.dto;

import java.util.UUID;

public class TaskUpdateRequest {

    private String title;
    private String description;
    private UUID assignedUserId; // null = unassigned (backlog)

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
}
