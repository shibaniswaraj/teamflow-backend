package com.teamflow.teamflow.dto.dashboard;

public record AdminQuickMetricsDTO(
        long totalTasks,
        long completedTasks,
        long totalProjects
) {}
