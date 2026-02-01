package com.teamflow.teamflow.controller;

import com.teamflow.teamflow.model.Role;
import com.teamflow.teamflow.model.User;
import com.teamflow.teamflow.security.JwtUtil;
import com.teamflow.teamflow.service.UserService;
import com.teamflow.teamflow.dto.LoginRequest;
import com.teamflow.teamflow.dto.CreateUserRequest;
import com.teamflow.teamflow.dto.ChangePasswordRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        User user = userService.validateLogin(
                request.getEmail(),
                request.getPassword()
        );

        String jwt = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        ResponseCookie cookie = ResponseCookie.from("TEAMFLOW_TOKEN", jwt)
                .httpOnly(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .secure(false) // DEV
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of(
                        "message", "Login successful",
                        "firstLogin", user.isFirstLogin(),
                        "role", user.getRole().name()
                ));
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody CreateUserRequest dto
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Only ADMIN can create users");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        userService.createUserAsAdmin(user);

        return ResponseEntity.status(201)
                .body(Map.of("message", "User created successfully"));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        userService.changePassword(
                request.getEmail(),
                request.getOldPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                Map.of("message", "Password changed successfully")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        ResponseCookie cookie = ResponseCookie.from("TEAMFLOW_TOKEN", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .secure(false)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        return ResponseEntity.ok(
                Map.of(
                        "email", auth.getName(),
                        "role", auth.getAuthorities().iterator().next().getAuthority()
                )
        );
    }

}
