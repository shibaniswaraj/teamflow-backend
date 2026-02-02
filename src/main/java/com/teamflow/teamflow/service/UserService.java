package com.teamflow.teamflow.service;

import com.teamflow.teamflow.dto.*;
import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // ===============================
    // ADMIN CREATES USER
    // ===============================
    public User createUserAsAdmin(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        String tempPassword = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setFirstLogin(true);
        user.setLoginCount(0);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        emailService.sendOnboardingEmail(
                user.getEmail(),
                tempPassword
        );

        return savedUser;
    }

    // ===============================
    // LOGIN VALIDATION
    // ===============================
    public User validateLogin(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.isActive()) {
            throw new RuntimeException("Account disabled");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        user.setLoginCount(user.getLoginCount() + 1);
        userRepository.save(user);

        return user;
    }

    // ===============================
    // CHANGE PASSWORD
    // ===============================
    public void changePassword(
            String email,
            String oldPassword,
            String newPassword
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);

        userRepository.save(user);
    }

    // ===============================
    // PAGED USERS (ADMIN)
    // ===============================
    // ===============================
// PAGED USERS (ADMIN)
// ===============================
    public Page<UserResponse> getUsersPaged(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository
                .findAllByOrderByUpdatedAtDesc(pageable) // ✅ CHANGED
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                ));
    }


    // ===============================
    // GET ALL MEMBERS (ADMIN / MANAGER)
    // ===============================
    public List<UserSummaryResponse> getAllMembers(Authentication auth) {

        String email = auth.getName();

        User currentUser = userRepository
                .findByEmail(email)
                .orElseThrow();

        if (currentUser.getRole() != Role.ADMIN &&
                currentUser.getRole() != Role.MANAGER) {
            throw new RuntimeException("Not authorized");
        }

        return userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == Role.MEMBER)
                .map(u -> new UserSummaryResponse(
                        u.getId(),
                        u.getName(),
                        u.getEmail()
                ))
                .toList();
    }


    // ===============================
    // FORGOT PASSWORD
    // ===============================
    public void requestPasswordReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String resetLink =
                "http://localhost:5173/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    // ===============================
    // RESET PASSWORD
    // ===============================
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        user.setFirstLogin(false);

        userRepository.save(user);
    }


    // ===============================
    // GET USER BY ID (ADMIN)
    // ===============================
    public UserDetailResponse getUserById(UUID userId, Authentication auth) {

        User admin = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can view user details");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    // ===============================
    // UPDATE USER (ADMIN)
    // ===============================
    public UserDetailResponse updateUser(
            UUID userId,
            UpdateUserRequest request,
            Authentication auth
    ) {
        User actingAdmin = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        if (actingAdmin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Only ADMIN can update users");
        }

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (targetUser.getRole() == Role.ADMIN &&
                !targetUser.getId().equals(actingAdmin.getId())) {
            throw new RuntimeException("Admins cannot modify other admins");
        }

        if (targetUser.getId().equals(actingAdmin.getId())
                && request.getRole() != Role.ADMIN) {
            throw new RuntimeException("Admin cannot demote self");
        }

        userRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(userId)) {
                        throw new RuntimeException("Email already in use");
                    }
                });

        targetUser.setName(request.getName());
        targetUser.setEmail(request.getEmail());
        targetUser.setRole(request.getRole());

        User saved = userRepository.save(targetUser);

        return new UserDetailResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    public UserResponse getMyProfile(Authentication auth) {
        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public UserResponse updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication auth
    ) {
        User user = userRepository
                .findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

}
