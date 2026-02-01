package com.teamflow.teamflow.service;

import com.teamflow.teamflow.dto.UserResponse;
import com.teamflow.teamflow.dto.UserSummaryResponse;
import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
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
    public Page<UserResponse> getUsersPaged(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return userRepository.findAll(pageable)
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
}
