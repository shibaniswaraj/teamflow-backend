package com.teamflow.teamflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    long countByRole(Role role);
    long count();


}
