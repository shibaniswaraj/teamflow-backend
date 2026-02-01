package com.teamflow.teamflow.controller;
import com.teamflow.teamflow.dto.ProjectMemberResponse;


import com.teamflow.teamflow.dto.ProjectCreateRequest;
import com.teamflow.teamflow.dto.ProjectResponse;
import com.teamflow.teamflow.dto.ProjectUpdateRequest;
import com.teamflow.teamflow.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // =========================================================
    // ✅ GET ALL PROJECTS (ADMIN / MANAGER / MEMBER)
    // =========================================================
//    @GetMapping
//    public List<ProjectResponse> getProjects(Authentication auth) {
//        return projectService.getProjects(auth);
//    }

    @GetMapping
    public Page<ProjectResponse> getProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Authentication authentication
    ) {
        return projectService.getProjectsPaged(page, size, authentication);
    }


    // =========================================================
    // ✅ CREATE PROJECT (ADMIN / MANAGER)
    // =========================================================
    @PostMapping
    public void createProject(
            @RequestBody ProjectCreateRequest request,
            Authentication auth
    ) {
        projectService.createProject(request, auth);
    }

    // =========================================================
    // ✅ GET PROJECT BY ID (OPEN / EDIT PAGE)
    // =========================================================
    @GetMapping("/{projectId}")
    public ProjectResponse getProjectById(
            @PathVariable UUID projectId,
            Authentication auth
    ) {
        return projectService.getProjectById(projectId, auth);
    }

    // =========================================================
    // ✅ UPDATE PROJECT (ADMIN / MANAGER)
    // =========================================================
    @PutMapping("/{projectId}")
    public void updateProject(
            @PathVariable UUID projectId,
            @RequestBody ProjectUpdateRequest request,
            Authentication auth
    ) {
        projectService.updateProject(projectId, request, auth);
    }


    @GetMapping("/{projectId}/members")
    public List<ProjectMemberResponse> getProjectMembers(
            @PathVariable UUID projectId,
            Authentication auth
    ) {
        return projectService.getProjectMembers(projectId, auth);
    }
}
