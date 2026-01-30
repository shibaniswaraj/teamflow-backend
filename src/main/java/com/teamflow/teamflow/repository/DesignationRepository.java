package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DesignationRepository extends JpaRepository<Designation, UUID> {
}
