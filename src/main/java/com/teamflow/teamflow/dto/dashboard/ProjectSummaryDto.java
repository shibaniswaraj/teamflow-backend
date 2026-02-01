package com.teamflow.teamflow.dto.dashboard;

import com.teamflow.teamflow.model.TaskStatus;

import java.util.Map;
import java.util.UUID;

public class ProjectSummaryDto {

    private UUID projectId;
    private String projectName;

    // Admin view
    private Long activeTasks;
    private Long totalMembers;

    // Manager / Member view
    private Map<TaskStatus, Long> taskStatusCount;

    // 🔹 Admin constructor
    public ProjectSummaryDto(
            UUID projectId,
            String projectName,
            Long activeTasks,
            Long totalMembers
    ) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.activeTasks = activeTasks;
        this.totalMembers = totalMembers;
    }

    // 🔹 Manager / Member constructor
    public ProjectSummaryDto(
            UUID projectId,
            String projectName,
            Map<TaskStatus, Long> taskStatusCount
    ) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.taskStatusCount = taskStatusCount;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public Long getActiveTasks() {
        return activeTasks;
    }

    public Long getTotalMembers() {
        return totalMembers;
    }

    public Map<TaskStatus, Long> getTaskStatusCount() {
        return taskStatusCount;
    }
}
