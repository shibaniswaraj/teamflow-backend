package com.teamflow.teamflow.dto;

import java.util.List;
import java.util.UUID;

public class ProjectResponse {

    private UUID id;
    private String name;

    private UUID managerId;
    private String managerName;

    private List<UUID> memberIds;
    private int memberCount;

    public ProjectResponse(
            UUID id,
            String name,
            UUID managerId,
            String managerName,
            List<UUID> memberIds
    ) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
        this.managerName = managerName;
        this.memberIds = memberIds;
        this.memberCount = memberIds.size();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public UUID getManagerId() { return managerId; }
    public String getManagerName() { return managerName; }
    public List<UUID> getMemberIds() { return memberIds; }
    public int getMemberCount() { return memberCount; }
}
