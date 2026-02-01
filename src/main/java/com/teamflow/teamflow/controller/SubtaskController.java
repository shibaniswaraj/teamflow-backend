package com.teamflow.teamflow.controller;

import com.teamflow.teamflow.dto.SubtaskCreateRequest;
import com.teamflow.teamflow.service.SubtaskService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @PostMapping
    public void createSubtask(
            @RequestBody SubtaskCreateRequest request,
            Authentication auth
    ) {
        subtaskService.createSubtask(request, auth);
    }
}
