package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.Project;
import com.teamflow.teamflow.model.ProjectMember;
import com.teamflow.teamflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

    List<ProjectMember> findByProject(Project project);

    List<ProjectMember> findByUser(User user);

}
