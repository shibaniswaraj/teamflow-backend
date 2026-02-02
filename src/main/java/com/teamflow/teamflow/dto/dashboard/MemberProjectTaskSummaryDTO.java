package com.teamflow.teamflow.dto.dashboard;

import com.teamflow.teamflow.model.TaskStatus;
import java.util.Map;
import java.util.UUID;

public record MemberProjectTaskSummaryDTO(
        UUID projectId,
        String projectName,
        long myTasks,
        Map<TaskStatus, Long> taskStatusCounts
) {}
