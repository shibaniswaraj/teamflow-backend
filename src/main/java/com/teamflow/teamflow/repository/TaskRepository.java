package com.teamflow.teamflow.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamflow.teamflow.model.Project;
import com.teamflow.teamflow.model.Task;
import com.teamflow.teamflow.model.TaskStatus;
import com.teamflow.teamflow.model.User;

public interface TaskRepository extends JpaRepository<Task, UUID> {

	List<Task> findByAssignedUser(User user);

	List<Task> findByProject(Project project);

	long countByStatus(TaskStatus status);

	long countByProject(Project project);

	List<Task> findByAssignedUserAndStatus(User user, TaskStatus status);

	List<Task> findByDeadlineBetween(LocalDate start, LocalDate end);

}
