package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.Project;
import com.teamflow.teamflow.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Page<Project> findAllByOrderByUpdatedAtDesc(Pageable pageable);

    Page<Project> findByManagerOrderByUpdatedAtDesc(
            User manager,
            Pageable pageable
    );




}
