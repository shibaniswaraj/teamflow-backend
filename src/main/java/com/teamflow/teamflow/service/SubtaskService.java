package com.teamflow.teamflow.service;

import com.teamflow.teamflow.dto.SubtaskCreateRequest;
import com.teamflow.teamflow.model.*;
import com.teamflow.teamflow.repository.SubtaskRepository;
import com.teamflow.teamflow.repository.TaskRepository;
import com.teamflow.teamflow.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public SubtaskService(
            SubtaskRepository subtaskRepository,
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User currentUser(Authentication auth) {
        Claims claims = (Claims) auth.getPrincipal();
        return userRepository.findByEmail(claims.getSubject()).orElseThrow();
    }

    // =========================
    // CREATE SUBTASK
    // =========================
    public void createSubtask(SubtaskCreateRequest request, Authentication auth) {

        User user = currentUser(auth);
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (user.getRole() == Role.MEMBER &&
                (task.getAssignedUser() == null ||
                        !task.getAssignedUser().getId().equals(user.getId()))) {
            throw new RuntimeException("Members can create subtasks only for their tasks");
        }

        Subtask subtask = new Subtask();
        subtask.setTitle(request.getTitle());
        subtask.setTask(task);

        subtaskRepository.save(subtask);
    }
}
