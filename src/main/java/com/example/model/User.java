package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

import javax.persistence.*;

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
