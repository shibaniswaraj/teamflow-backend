package com.teamflow.teamflow.dto.dashboard;

import java.util.UUID;

public record AdminTopProjectDTO(
        UUID projectId,
        String projectName,
        long activeTasks,
        long totalUsers
) {}
