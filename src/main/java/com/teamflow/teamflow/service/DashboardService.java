package com.teamflow.teamflow.service;

import com.teamflow.teamflow.dto.dashboard.*;
import com.teamflow.teamflow.model.*;
import com.teamflow.teamflow.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public DashboardService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            TaskRepository taskRepository,
            ProjectMemberRepository projectMemberRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    // ================= SECURITY =================
    public void assertRole(Authentication auth, Role role) {
        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        if (user.getRole() != role) {
            throw new RuntimeException("Not authorized");
        }
    }

    // ================= ADMIN =================
    public AdminDashboardResponse getAdminDashboard() {

        long totalUsers = userRepository.count();
        long managers = userRepository.findAll().stream().filter(u -> u.getRole() == Role.MANAGER).count();
        long members = userRepository.findAll().stream().filter(u -> u.getRole() == Role.MEMBER).count();
        long totalProjects = projectRepository.count();

        List<AdminTopProjectDTO> topProjects =
                projectRepository.findAll().stream()
                        .sorted(Comparator.comparing(Project::getUpdatedAt).reversed())
                        .limit(5)
                        .map(p -> {
                            long activeTasks = taskRepository.findByProject(p)
                                    .stream()
                                    .filter(t -> t.getStatus() != TaskStatus.DONE)
                                    .count();

                            long totalUsersInProject =
                                    projectMemberRepository.findByProject(p).size() + 1; // + manager

                            return new AdminTopProjectDTO(
                                    p.getId(),
                                    p.getName(),
                                    activeTasks,
                                    totalUsersInProject
                            );
                        })
                        .toList();

        AdminQuickMetricsDTO metrics = new AdminQuickMetricsDTO(
                taskRepository.count(),
                taskRepository.findAll().stream().filter(t -> t.getStatus() == TaskStatus.DONE).count(),
                projectRepository.count()
        );

        return new AdminDashboardResponse(
                totalProjects,
                totalUsers,
                managers,
                members,
                topProjects,
                metrics
        );
    }

    // ================= MANAGER =================
    public ManagerDashboardResponse getManagerDashboard(String email) {

        User manager = userRepository.findByEmail(email).orElseThrow();

        List<ManagedProjectDTO> projects =
                projectRepository.findByManagerOrderByUpdatedAtDesc(manager, null)
                        .stream()
                        .map(p -> {
                            List<Task> tasks = taskRepository.findByProject(p);

                            Map<TaskStatus, Long> statusMap =
                                    tasks.stream()
                                            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

                            return new ManagedProjectDTO(
                                    p.getId(),
                                    p.getName(),
                                    projectMemberRepository.findByProject(p).size(),
                                    statusMap
                            );
                        })
                        .toList();

        return new ManagerDashboardResponse(projects);
    }

    // ================= MEMBER =================
    public MemberDashboardResponse getMemberDashboard(String email) {

        User member = userRepository.findByEmail(email).orElseThrow();

        List<MemberProjectTaskSummaryDTO> projects =
                projectMemberRepository.findByUser(member)
                        .stream()
                        .map(pm -> {
                            Project p = pm.getProject();
                            List<Task> tasks = taskRepository.findByAssignedUser(member)
                                    .stream()
                                    .filter(t -> t.getProject().getId().equals(p.getId()))
                                    .toList();

                            Map<TaskStatus, Long> statusMap =
                                    tasks.stream()
                                            .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

                            return new MemberProjectTaskSummaryDTO(
                                    p.getId(),
                                    p.getName(),
                                    tasks.size(),
                                    statusMap
                            );
                        })
                        .toList();

        return new MemberDashboardResponse(projects);
    }
}
