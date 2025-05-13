package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
public class User {

    @Id
    private String userID;
    private String user;
    private String email;
    private String pass;

    @ManyToOne
    @JoinColumn(name = "profileID")
    private Profile profile;


    public User(){
    }

    public User(String user, String pass, String email){
        this.userID = UUID.randomUUID().toString();
        this.user = user;
        this.email = email;
        this.pass = pass;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserId() {
        return userID;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }
}
