package com.teamflow.teamflow.model;
import jakarta.persistence.*;
import java.util.UUID;

@Entity //marks this class as a jpa entity i.e., now it's a table
@Table(name="users") //to give custom name="users", otherwise it would've been only user
public class User {

    @Id //for primary key
    @GeneratedValue //for auto generating values
    private UUID id;

    @Column(nullable = false)
    private String name;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) //Stores enum values as strings
    @Column(nullable = false)
    private Role role;

    @ManyToOne  //multiple users can have same designation
    @JoinColumn(name = "designation_id") //referencing foreign key from designation table, there the name of the column will still be id, only in users table the name of the column that store designation ids will be named as designation_id
    private Designation designation; //we ar storing id not the text of designation

    @Column(nullable = false)
    private boolean firstLogin = true;   // forces password change

    @Column(nullable = false)
    private int loginCount = 0;          // track logins

    @Column(nullable = false)
    private boolean active = true;       // disable user if needed


    // ---- Default Constructor (Required by Hibernate) ----
    public User() {}

    // ---- All-Args Constructor (optional helper for manual creation) ----
    public User(String name,String email, String password, Role role, Designation designation) {
        this.name=name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.designation = designation;
    }

    // ---- Getters & Setters (Hibernate + Spring use these internally) ----
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

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
