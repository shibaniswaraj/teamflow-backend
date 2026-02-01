package com.teamflow.teamflow.dto;

import com.teamflow.teamflow.model.TaskStatus;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;

    private UUID assignedUserId;
    private String assignedUserName;
    private String assignedUserEmail; // ✅ REQUIRED FOR COLOR CONSISTENCY

    public TaskResponse(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            UUID assignedUserId,
            String assignedUserName,
            String assignedUserEmail
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedUserId = assignedUserId;
        this.assignedUserName = assignedUserName;
        this.assignedUserEmail = assignedUserEmail;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }

    public UUID getAssignedUserId() { return assignedUserId; }
    public String getAssignedUserName() { return assignedUserName; }
    public String getAssignedUserEmail() { return assignedUserEmail; }
}
