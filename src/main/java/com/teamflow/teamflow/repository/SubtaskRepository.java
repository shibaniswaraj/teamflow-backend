package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.Subtask;
import com.teamflow.teamflow.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubtaskRepository extends JpaRepository<Subtask, UUID> {

    List<Subtask> findByTask(Task task);

}
