package com.teamflow.teamflow.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.teamflow.teamflow.dto.dashboard.ActivityDto;
import com.teamflow.teamflow.dto.dashboard.AdminDashboardResponse;
import com.teamflow.teamflow.dto.dashboard.DeadlineDto;
import com.teamflow.teamflow.dto.dashboard.ManagerDashboardResponse;
import com.teamflow.teamflow.dto.dashboard.MemberDashboardResponse;
import com.teamflow.teamflow.dto.dashboard.ProjectSummaryDto;
import com.teamflow.teamflow.model.ActivityLog;
import com.teamflow.teamflow.model.Project;
import com.teamflow.teamflow.model.ProjectMember;
import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.Task;
import com.teamflow.teamflow.model.TaskStatus;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.repository.ActivityLogRepository;
import com.teamflow.teamflow.repository.ProjectMemberRepository;
import com.teamflow.teamflow.repository.ProjectRepository;
import com.teamflow.teamflow.repository.TaskRepository;
import com.teamflow.teamflow.repository.UserRepository;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final TaskRepository taskRepository;
    private final ActivityLogRepository activityLogRepository;

    public DashboardService(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            TaskRepository taskRepository,
            ActivityLogRepository activityLogRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.taskRepository = taskRepository;
        this.activityLogRepository = activityLogRepository;
    }

    // 🔥 ENTRY POINT FROM CONTROLLER
    public Object getDashboardForRole(String email, String role) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return switch (role) {
            case "ADMIN" -> buildAdminDashboard();
            case "MANAGER" -> buildManagerDashboard(user);
            default -> buildMemberDashboard(user);
        };
    }

    // ===================== ADMIN DASHBOARD =====================
    private AdminDashboardResponse buildAdminDashboard() {

        long totalProjects = projectRepository.count();
        long totalUsers = userRepository.count();

        long managers = userRepository.countByRole(Role.MANAGER);
        long members  = userRepository.countByRole(Role.MEMBER);

        List<ProjectSummaryDto> topProjects =
                projectRepository.findTop5ByOrderByUpdatedAtDesc()
                        .stream()
                        .map(project -> {
                            long activeTasks =
                                    taskRepository.findByProject(project)
                                            .stream()
                                            .filter(t -> t.getStatus() != TaskStatus.DONE)
                                            .count();

                            long totalMembers =
                                    projectMemberRepository
                                            .findByProject(project)
                                            .size();

                            return new ProjectSummaryDto(
                                    project.getId(),
                                    project.getName(),
                                    activeTasks,
                                    totalMembers
                            );
                        })
                        .collect(Collectors.toList());

        List<DeadlineDto> upcomingDeadlines =
                taskRepository.findByDeadlineBetween(
                                LocalDate.now(),
                                LocalDate.now().plusDays(5)
                        )
                        .stream()
                        .map(task -> new DeadlineDto(
                                task.getId(),
                                task.getTitle(),
                                task.getDeadline()
                        ))
                        .collect(Collectors.toList());

        List<ActivityDto> recentActivities =
                activityLogRepository
                        .findTop10ByOrderByCreatedAtDesc()
                        .stream()
                        .map(log -> new ActivityDto(
                                log.getDescription(),
                                log.getCreatedAt()
                        ))
                        .collect(Collectors.toList());

        return new AdminDashboardResponse(
                totalProjects,
                totalUsers,
                managers,
                members,
                topProjects,
                upcomingDeadlines,
                recentActivities
        );
    }

    // ===================== MANAGER DASHBOARD =====================
    private ManagerDashboardResponse buildManagerDashboard(User manager) {

        List<Project> managedProjects =
                projectRepository.findByManager(manager);

        List<ProjectSummaryDto> projects =
                managedProjects.stream()
                        .map(project -> {
                            Map<TaskStatus, Long> statusCount =
                                    taskRepository.findByProject(project)
                                            .stream()
                                            .collect(Collectors.groupingBy(
                                                    Task::getStatus,
                                                    Collectors.counting()
                                            ));

                            return new ProjectSummaryDto(
                                    project.getId(),
                                    project.getName(),
                                    statusCount
                            );
                        })
                        .collect(Collectors.toList());

        List<DeadlineDto> upcomingDeadlines =
                taskRepository.findByDeadlineBetween(
                                LocalDate.now(),
                                LocalDate.now().plusDays(5)
                        )
                        .stream()
                        .filter(task -> managedProjects.contains(task.getProject()))
                        .map(task -> new DeadlineDto(
                                task.getId(),
                                task.getTitle(),
                                task.getDeadline()
                        ))
                        .collect(Collectors.toList());

        List<ActivityDto> activities =
                activityLogRepository.findAll()
                        .stream()
                        .filter(log ->
                                log.getProject() != null &&
                                managedProjects.contains(log.getProject())
                        )
                        .sorted(Comparator.comparing(ActivityLog::getCreatedAt).reversed())
                        .limit(10)
                        .map(log -> new ActivityDto(
                                log.getDescription(),
                                log.getCreatedAt()
                        ))
                        .collect(Collectors.toList());

        return new ManagerDashboardResponse(
                projects,
                upcomingDeadlines,
                activities
        );
    }

    // ===================== MEMBER DASHBOARD =====================
    private MemberDashboardResponse buildMemberDashboard(User user) {

        List<Project> myProjects =
                projectMemberRepository.findByUser(user)
                        .stream()
                        .map(ProjectMember::getProject)
                        .collect(Collectors.toList());

        List<ProjectSummaryDto> projectSummaries =
                myProjects.stream()
                        .map(project -> {
                            Map<TaskStatus, Long> statusCount =
                                    taskRepository.findByAssignedUser(user)
                                            .stream()
                                            .filter(task -> task.getProject().equals(project))
                                            .collect(Collectors.groupingBy(
                                                    Task::getStatus,
                                                    Collectors.counting()
                                            ));

                            return new ProjectSummaryDto(
                                    project.getId(),
                                    project.getName(),
                                    statusCount
                            );
                        })
                        .collect(Collectors.toList());

        List<DeadlineDto> myDeadlines =
                taskRepository.findByAssignedUser(user)
                        .stream()
                        .filter(task ->
                                task.getDeadline() != null &&
                                !task.getDeadline().isBefore(LocalDate.now())
                        )
                        .map(task -> new DeadlineDto(
                                task.getId(),
                                task.getTitle(),
                                task.getDeadline()
                        ))
                        .collect(Collectors.toList());

        List<ActivityDto> teamActivity =
                activityLogRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(ActivityLog::getCreatedAt).reversed())
                        .limit(10)
                        .map(log -> new ActivityDto(
                                log.getDescription(),
                                log.getCreatedAt()
                        ))
                        .collect(Collectors.toList());

        return new MemberDashboardResponse(
                projectSummaries,
                myDeadlines,
                teamActivity
        );
    }
}
