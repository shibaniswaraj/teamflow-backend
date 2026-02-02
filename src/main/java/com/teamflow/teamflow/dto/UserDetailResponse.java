package com.teamflow.teamflow.dto;

import com.teamflow.teamflow.model.Role;

import java.util.UUID;

public class UserDetailResponse {

    private UUID id;
    private String name;
    private String email;
    private Role role;

    public UserDetailResponse(
            UUID id,
            String name,
            String email,
            Role role
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
}