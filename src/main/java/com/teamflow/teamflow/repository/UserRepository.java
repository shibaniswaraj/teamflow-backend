package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    long countByRole(Role role);

    long count();
}