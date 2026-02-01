package com.teamflow.teamflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamflow.teamflow.service.DashboardService;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<?> getDashboard() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // 🔒 Safety check (should never fail if JWT filter works)
        if (authentication == null || authentication.getDetails() == null) {
            return ResponseEntity.status(401).build();
        }

        // 🔑 Extract JWT claims (you stored this in JwtFilter)
        Claims claims = (Claims) authentication.getDetails();

        String email = claims.getSubject();               // user identity
        String role  = claims.get("role", String.class); // ADMIN / MANAGER / USER

        // 🧠 Delegate role-based decision to service
        Object dashboardResponse = dashboardService
                .getDashboardForRole(email, role);

        return ResponseEntity.ok(dashboardResponse);
    }
}
