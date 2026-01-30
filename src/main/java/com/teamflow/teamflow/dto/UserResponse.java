package com.teamflow.teamflow.dto;

import com.teamflow.teamflow.model.Role;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Role role
) {}
