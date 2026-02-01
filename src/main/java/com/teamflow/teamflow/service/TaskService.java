package com.teamflow.teamflow.service;

import com.teamflow.teamflow.dto.*;
import com.teamflow.teamflow.model.*;
import com.teamflow.teamflow.repository.ProjectRepository;
import com.teamflow.teamflow.repository.TaskRepository;
import com.teamflow.teamflow.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // ===============================
    // CREATE TASK
    // ===============================
    public TaskResponse createTask(TaskCreateRequest request, Authentication auth) {

        User creator = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        if (creator.getRole() != Role.MANAGER) {
            throw new RuntimeException("Only MANAGER can create tasks");
        }

        Project project = projectRepository
                .findById(request.getProjectId())
                .orElseThrow();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setProject(project);

        if (request.getAssignedUserId() == null) {
            task.setAssignedUser(null);
            task.setStatus(TaskStatus.TODO);
        } else {
            User assignee = userRepository
                    .findById(request.getAssignedUserId())
                    .orElseThrow();
            task.setAssignedUser(assignee);
            task.setStatus(request.getStatus());
        }

        return map(taskRepository.save(task));
    }

    // ===============================
    // UPDATE TASK (EDIT)
    // ===============================
    public TaskResponse updateTask(
            UUID taskId,
            TaskUpdateRequest request,
            Authentication auth
    ) {
        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        if (user.getRole() != Role.MANAGER) {
            throw new RuntimeException("Only MANAGER can edit tasks");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getAssignedUserId() == null) {
            task.setAssignedUser(null); // backlog
        } else {
            User assignee = userRepository
                    .findById(request.getAssignedUserId())
                    .orElseThrow();
            task.setAssignedUser(assignee);
        }

        return map(taskRepository.save(task));
    }

    // ===============================
    // MOVE TASK (STATUS)
    // ===============================
    public TaskResponse moveTask(
            UUID taskId,
            TaskMoveRequest request,
            Authentication auth
    ) {
        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        if (user.getRole() != Role.MANAGER && user.getRole() != Role.MEMBER) {
            throw new RuntimeException("Not allowed to change task status");
        }


        Task task = taskRepository.findById(taskId)
                .orElseThrow();

        task.setStatus(request.getStatus());

        return map(taskRepository.save(task));
    }

    // ===============================
    // GET PROJECT TASKS
    // ===============================
    public List<TaskResponse> getTasksByProject(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();

        return taskRepository.findByProject(project)
                .stream()
                .map(this::map)
                .toList();
    }

    private TaskResponse map(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAssignedUser() != null ? task.getAssignedUser().getId() : null,
                task.getAssignedUser() != null
                        ? (task.getAssignedUser().getName() != null
                        ? task.getAssignedUser().getName()
                        : task.getAssignedUser().getEmail())
                        : null,
                task.getAssignedUser() != null
                        ? task.getAssignedUser().getEmail()
                        : null
        );
    }
}
