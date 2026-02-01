package com.teamflow.teamflow.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamflow.teamflow.model.ActivityLog;
import com.teamflow.teamflow.model.User;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {

    List<ActivityLog> findByUser(User user);
    
    List<ActivityLog> findTop10ByOrderByCreatedAtDesc();


}
