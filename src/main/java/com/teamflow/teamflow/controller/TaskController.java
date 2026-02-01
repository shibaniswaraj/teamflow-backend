package com.teamflow.teamflow.controller;

import com.teamflow.teamflow.dto.*;
import com.teamflow.teamflow.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(
            @RequestBody TaskCreateRequest request,
            Authentication auth
    ) {
        return taskService.createTask(request, auth);
    }

    // ✅ EDIT TASK
    @PutMapping("/{taskId}")
    public TaskResponse updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskUpdateRequest request,
            Authentication auth
    ) {
        return taskService.updateTask(taskId, request, auth);
    }

    // ✅ MOVE TASK
    @PutMapping("/{taskId}/move")
    public TaskResponse moveTask(
            @PathVariable UUID taskId,
            @RequestBody TaskMoveRequest request,
            Authentication auth
    ) {
        return taskService.moveTask(taskId, request, auth);
    }

    @GetMapping("/project/{projectId}")
    public List<TaskResponse> getProjectTasks(
            @PathVariable UUID projectId
    ) {
        return taskService.getTasksByProject(projectId);
    }
}
