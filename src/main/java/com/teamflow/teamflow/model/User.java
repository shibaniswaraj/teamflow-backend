package com.teamflow.teamflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "designation_id")
    private Designation designation;

    @Column(nullable = false)
    private boolean firstLogin = true;

    @Column(nullable = false)
    private int loginCount = 0;

    @Column(nullable = false)
    private boolean active = true;

    // 🔑 FORGOT PASSWORD
    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    // ✅ NEW — for ordering users
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public User() {}

    public User(String name, String email, String password, Role role, Designation designation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.designation = designation;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Designation getDesignation() { return designation; }
    public void setDesignation(Designation designation) { this.designation = designation; }

    public boolean isFirstLogin() { return firstLogin; }
    public void setFirstLogin(boolean firstLogin) { this.firstLogin = firstLogin; }

    public int getLoginCount() { return loginCount; }
    public void setLoginCount(int loginCount) { this.loginCount = loginCount; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public LocalDateTime getResetTokenExpiry() { return resetTokenExpiry; }
    public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
