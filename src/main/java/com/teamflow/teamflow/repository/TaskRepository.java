package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.Task;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByAssignedUser(User user);

    List<Task> findByProject(Project project);
    List<Task> findByAssignedUserIsNull();


}
