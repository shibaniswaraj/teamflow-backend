package com.teamflow.teamflow.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

public class ProjectCreateRequest {

    @NotBlank
    private String name;

    // ADMIN only
    private UUID managerId;

    // MANAGER only (optional during create)
    private List<UUID> memberIds;

    public String getName() {
        return name;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public List<UUID> getMemberIds() {
        return memberIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManagerId(UUID managerId) {
        this.managerId = managerId;
    }

    public void setMemberIds(List<UUID> memberIds) {
        this.memberIds = memberIds;
    }
}
