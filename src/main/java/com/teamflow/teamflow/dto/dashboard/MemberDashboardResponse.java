package com.teamflow.teamflow.dto.dashboard;

import java.util.List;

public class MemberDashboardResponse {

    private List<ProjectSummaryDto> myProjects;
    private List<DeadlineDto> myDeadlines;
    private List<ActivityDto> teamActivity;

    public MemberDashboardResponse(
            List<ProjectSummaryDto> myProjects,
            List<DeadlineDto> myDeadlines,
            List<ActivityDto> teamActivity
    ) {
        this.myProjects = myProjects;
        this.myDeadlines = myDeadlines;
        this.teamActivity = teamActivity;
    }

    public List<ProjectSummaryDto> getMyProjects() {
        return myProjects;
    }

    public List<DeadlineDto> getMyDeadlines() {
        return myDeadlines;
    }

    public List<ActivityDto> getTeamActivity() {
        return teamActivity;
    }
}
