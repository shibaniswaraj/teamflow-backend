package com.teamflow.teamflow.model;
import jakarta.persistence.*;
import java.util.UUID;

@Entity //marks this class as a jpa entity i.e., now it's a table
@Table(name="users") //to give custom name="users", otherwise it would've been only user
public class User {

    @Id //for primary key
    @GeneratedValue //for auto generating values
    private UUID id;



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


    // ---- Default Constructor (Required by Hibernate) ----
    public User() {}

    // ---- All-Args Constructor (optional helper for manual creation) ----
    public User(String email, String password, Role role, Designation designation) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.designation = designation;
    }

    // ---- Getters & Setters (Hibernate + Spring use these internally) ----
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Designation getDesignation() { return designation; }
    public void setDesignation(Designation designation) { this.designation = designation; }

}
