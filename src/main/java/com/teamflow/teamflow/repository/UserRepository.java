package com.teamflow.teamflow.repository;

import com.teamflow.teamflow.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    // ✅ NEW — latest users first
    Page<User> findAllByOrderByUpdatedAtDesc(Pageable pageable);
}
