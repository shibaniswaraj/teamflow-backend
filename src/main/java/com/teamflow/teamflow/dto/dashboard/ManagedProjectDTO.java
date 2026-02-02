package com.teamflow.teamflow.dto.dashboard;

import com.teamflow.teamflow.model.TaskStatus;
import java.util.Map;
import java.util.UUID;

public record ManagedProjectDTO(
        UUID projectId,
        String projectName,
        int membersCount,
        Map<TaskStatus, Long> taskStatusCounts
) {}
