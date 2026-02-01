package com.teamflow.teamflow.dto.dashboard;

import java.util.List;

public class ManagerDashboardResponse {

    private List<ProjectSummaryDto> projects;
    private List<DeadlineDto> upcomingDeadlines;
    private List<ActivityDto> recentActivities;

    public ManagerDashboardResponse(
            List<ProjectSummaryDto> projects,
            List<DeadlineDto> upcomingDeadlines,
            List<ActivityDto> recentActivities
    ) {
        this.projects = projects;
        this.upcomingDeadlines = upcomingDeadlines;
        this.recentActivities = recentActivities;
    }

    public List<ProjectSummaryDto> getProjects() {
        return projects;
    }

    public List<DeadlineDto> getUpcomingDeadlines() {
        return upcomingDeadlines;
    }

    public List<ActivityDto> getRecentActivities() {
        return recentActivities;
    }
}
