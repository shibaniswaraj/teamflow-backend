package com.teamflow.teamflow.model;
import jakarta.persistence.*;
// JPA annotations that map this class to a database table.

import java.util.UUID;

@Entity // Marks this class as a database entity (table in DB).
@Table(name="designations")// Sets the table name as "designations".
public class Designation {

    @Id //Primary Key column
    @GeneratedValue // auto generates id
    private UUID id;

    @Column(nullable = false , unique = true)
//    nullable=false, this field cant be empty
//    unique true, no two designations have the same name
    private String name; //eg: backend developer,  qa tester,etc.

//    default constructor required by hibernate
    public Designation(){

    }

    public Designation(String name){
        this.name=name;
    }

    //setters:(setters and getters are required by hibernate to read or write files)
    public void setName(String name){
        this.name=name;
    }

    public void setUUID(UUID id){
        this.id=id;
    }

    //getters
    public String getName(){
        return name;
    }

    public UUID getId(){
        return id;
    }
}
