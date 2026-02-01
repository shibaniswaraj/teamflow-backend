package com.teamflow.teamflow.service;
import com.teamflow.teamflow.dto.ProjectMemberResponse;

import com.teamflow.teamflow.dto.ProjectCreateRequest;
import com.teamflow.teamflow.dto.ProjectResponse;
import com.teamflow.teamflow.dto.ProjectUpdateRequest;
import com.teamflow.teamflow.model.Project;
import com.teamflow.teamflow.model.ProjectMember;
import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.repository.ProjectMemberRepository;
import com.teamflow.teamflow.repository.ProjectRepository;
import com.teamflow.teamflow.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ProjectMemberRepository projectMemberRepository
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    // ===============================
    // GET PROJECTS (PAGED)
    // ===============================
    public Page<ProjectResponse> getProjectsPaged(
            int page,
            int size,
            Authentication authentication
    ) {
        String email = authentication.getName();


        User currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projectPage;

        if (currentUser.getRole() == Role.ADMIN) {

            projectPage = projectRepository
                    .findAllByOrderByUpdatedAtDesc(pageable);

        } else if (currentUser.getRole() == Role.MANAGER) {

            projectPage = projectRepository
                    .findByManagerOrderByUpdatedAtDesc(currentUser, pageable);

        } else {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Members cannot list projects"
            );
        }

        return projectPage.map(project -> {

            List<UUID> memberIds = projectMemberRepository
                    .findByProject(project)
                    .stream()
                    .map(pm -> pm.getUser().getId())
                    .toList();

            return new ProjectResponse(
                    project.getId(),
                    project.getName(),
                    project.getManager().getId(),
                    project.getManager().getName(),
                    memberIds
            );
        });
    }

    // ===============================
    // GET PROJECT BY ID
    // ===============================
    public ProjectResponse getProjectById(UUID projectId, Authentication auth) {

        String email = auth.getName();


        User currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (currentUser.getRole() == Role.MANAGER &&
                !project.getManager().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized");
        }

        if (currentUser.getRole() == Role.MEMBER) {
            boolean isMember = projectMemberRepository
                    .findByProject(project)
                    .stream()
                    .anyMatch(pm -> pm.getUser().getId().equals(currentUser.getId()));

            if (!isMember) {
                throw new RuntimeException("Not authorized");
            }
        }

        List<UUID> memberIds = projectMemberRepository
                .findByProject(project)
                .stream()
                .map(pm -> pm.getUser().getId())
                .toList();

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getManager().getId(),
                project.getManager().getName(),
                memberIds
        );
    }

    // ===============================
    // CREATE PROJECT
    // ===============================
    public void createProject(ProjectCreateRequest request, Authentication auth) {

        String email = auth.getName();


        User currentUser = userRepository
                .findByEmail(email)
                .orElseThrow();

        Project project = new Project();
        project.setName(request.getName());

        if (currentUser.getRole() == Role.ADMIN) {

            if (request.getManagerId() == null) {
                throw new RuntimeException("Manager is required");
            }

            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            if (manager.getRole() != Role.MANAGER) {
                throw new RuntimeException("Assigned user is not a MANAGER");
            }

            project.setManager(manager);

        } else if (currentUser.getRole() == Role.MANAGER) {

            project.setManager(currentUser);

        } else {
            throw new RuntimeException("Not authorized");
        }
        project.setUpdatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        if (currentUser.getRole() == Role.MANAGER &&
                request.getMemberIds() != null) {

            for (UUID memberId : request.getMemberIds()) {

                User member = userRepository.findById(memberId)
                        .orElseThrow();

                if (member.getRole() != Role.MEMBER) continue;

                projectMemberRepository.save(
                        new ProjectMember(savedProject, member)
                );
            }
        }
    }

    // ===============================
    // UPDATE PROJECT
    // ===============================
    public void updateProject(
            UUID projectId,
            ProjectUpdateRequest request,
            Authentication auth
    ) {
        String email = auth.getName();


        User currentUser = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (request.getName() != null) {
            project.setName(request.getName());
        }

        if (currentUser.getRole() == Role.ADMIN && request.getManagerId() != null) {

            User newManager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));

            if (newManager.getRole() != Role.MANAGER) {
                throw new RuntimeException("Assigned user is not a MANAGER");
            }

            project.setManager(newManager);
        }

        if (currentUser.getRole() == Role.MANAGER) {

            if (!project.getManager().getId().equals(currentUser.getId())) {
                throw new RuntimeException("Not your project");
            }

            if (request.getMemberIds() != null) {

                projectMemberRepository.deleteAll(
                        projectMemberRepository.findByProject(project)
                );

                for (UUID memberId : request.getMemberIds()) {

                    User member = userRepository.findById(memberId)
                            .orElseThrow(() -> new RuntimeException("User not found"));

                    projectMemberRepository.save(
                            new ProjectMember(project, member)
                    );
                }
            }
        }
        project.setUpdatedAt(LocalDateTime.now());

        projectRepository.save(project);
    }



    public List<ProjectMemberResponse> getProjectMembers(
            UUID projectId,
            Authentication auth
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User currentUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 Access check
        if (currentUser.getRole() == Role.MANAGER &&
                !project.getManager().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized");
        }

        if (currentUser.getRole() == Role.MEMBER) {
            boolean isMember = projectMemberRepository
                    .findByProject(project)
                    .stream()
                    .anyMatch(pm -> pm.getUser().getId().equals(currentUser.getId()));

            if (!isMember) {
                throw new RuntimeException("Not authorized");
            }
        }

        return projectMemberRepository
                .findByProject(project)
                .stream()
                .map(pm -> new ProjectMemberResponse(
                        pm.getUser().getId(),
                        pm.getUser().getName(),
                        pm.getUser().getEmail()
                ))
                .toList();
    }
}
