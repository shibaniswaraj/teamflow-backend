package com.teamflow.teamflow.controller;

import com.teamflow.teamflow.dto.dashboard.*;
import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    public AdminDashboardResponse admin(Authentication auth) {
        dashboardService.assertRole(auth, Role.ADMIN);
        return dashboardService.getAdminDashboard();
    }

    @GetMapping("/manager")
    public ManagerDashboardResponse manager(Authentication auth) {
        dashboardService.assertRole(auth, Role.MANAGER);
        return dashboardService.getManagerDashboard(auth.getName());
    }

    @GetMapping("/member")
    public MemberDashboardResponse member(Authentication auth) {
        dashboardService.assertRole(auth, Role.MEMBER);
        return dashboardService.getMemberDashboard(auth.getName());
    }
}
