package com.teamflow.teamflow.dto.dashboard;

import java.time.LocalDateTime;

public class ActivityDto {

    private String description;
    private LocalDateTime createdAt;

    public ActivityDto(
            String description,
            LocalDateTime createdAt
    ) {
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
