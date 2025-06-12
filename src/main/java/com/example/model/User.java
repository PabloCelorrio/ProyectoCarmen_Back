package com.example.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "userID", nullable = false)
    private String userID;

    private String userName;
    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL) // esto guarda el profile automáticamente
    @JoinColumn(name = "profileID", referencedColumnName = "profileID")
    @JsonManagedReference
    private Profile profile;

    public User(String userName, String password, String email, Profile profile){
        this.userID = UUID.randomUUID().toString();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userID;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Profile getProfile() { return profile; }

    public void setProfile(Profile profile) { this.profile = profile; }
}

