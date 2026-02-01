package com.teamflow.teamflow.model;

import jakarta.persistence.*; // JPA annotations (@Entity, @Table, @Id, @ManyToOne etc.)
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

import java.util.UUID;

@Entity // Marks this class as a database table
@Table(name = "projects") // Table name in DB
public class Project {

    @Id // Marks primary key column
    @GeneratedValue // UUID will auto-generate
    private UUID id;

    @Column(nullable = false) // Cannot be NULL
    private String name;

    private String description;

    @ManyToOne // Many projects can have ONE manager
    @JoinColumn(name = "manager_id", nullable = false) // foreign key in projects table → users table
    private User manager;

    // Auto-managed timestamps
    private LocalDateTime createdAt; // When project was first created
    private LocalDateTime updatedAt; // When project was last updated

    /**
     * @PrePersist runs automatically BEFORE Hibernate INSERTS a new row
     * ✔ sets createdAt the first time record is saved
     * ✔ sets updatedAt initially same as createdAt
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * @PreUpdate runs automatically BEFORE Hibernate UPDATES existing row
     * ✔ refreshes updatedAt timestamp whenever change happens in row
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Getters & Setters below =====

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
