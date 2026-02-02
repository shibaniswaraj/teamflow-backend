package com.teamflow.teamflow.dto.dashboard;

import java.util.List;

public record ManagerDashboardResponse(
        List<ManagedProjectDTO> projects
) {}
