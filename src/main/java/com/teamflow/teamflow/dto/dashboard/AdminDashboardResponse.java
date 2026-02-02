package com.teamflow.teamflow.dto.dashboard;

import java.util.List;

public record AdminDashboardResponse(
        long totalProjects,
        long totalUsers,
        long totalManagers,
        long totalMembers,
        List<AdminTopProjectDTO> topProjects,
        AdminQuickMetricsDTO quickMetrics
) {}
