package com.teamflow.teamflow.dto.dashboard;

import java.util.List;

public class AdminDashboardResponse {

    private long totalProjects;
    private long totalUsers;
    private long totalManagers;
    private long totalMembers;

    private List<ProjectSummaryDto> topProjects;
    private List<DeadlineDto> upcomingDeadlines;
    private List<ActivityDto> recentActivities;

    public AdminDashboardResponse(
            long totalProjects,
            long totalUsers,
            long totalManagers,
            long totalMembers,
            List<ProjectSummaryDto> topProjects,
            List<DeadlineDto> upcomingDeadlines,
            List<ActivityDto> recentActivities
    ) {
        this.totalProjects = totalProjects;
        this.totalUsers = totalUsers;
        this.totalManagers = totalManagers;
        this.totalMembers = totalMembers;
        this.topProjects = topProjects;
        this.upcomingDeadlines = upcomingDeadlines;
        this.recentActivities = recentActivities;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalManagers() {
        return totalManagers;
    }

    public long getTotalMembers() {
        return totalMembers;
    }

    public List<ProjectSummaryDto> getTopProjects() {
        return topProjects;
    }

    public List<DeadlineDto> getUpcomingDeadlines() {
        return upcomingDeadlines;
    }

    public List<ActivityDto> getRecentActivities() {
        return recentActivities;
    }
}
