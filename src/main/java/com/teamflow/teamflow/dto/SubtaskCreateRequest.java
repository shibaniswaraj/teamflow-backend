package com.teamflow.teamflow.dto;

import java.util.UUID;

public class SubtaskCreateRequest {

    private UUID taskId;
    private String title;

    public UUID getTaskId() { return taskId; }
    public String getTitle() { return title; }

    public void setTaskId(UUID taskId) { this.taskId = taskId; }
    public void setTitle(String title) { this.title = title; }
}
