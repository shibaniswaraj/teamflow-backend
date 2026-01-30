package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.ActivityLog;
import com.teamflow.teamflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {

    List<ActivityLog> findByUser(User user);

}
