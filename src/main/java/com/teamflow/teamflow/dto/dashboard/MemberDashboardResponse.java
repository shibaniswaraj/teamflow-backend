package com.teamflow.teamflow.dto.dashboard;

import java.util.List;

public record MemberDashboardResponse(
        List<MemberProjectTaskSummaryDTO> projects
) {}
