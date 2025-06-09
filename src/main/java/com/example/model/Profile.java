package com.example.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Profile {
    @Id
    private String profileID;
    private String nameAlias;

    @Lob
    private byte[] profileImg;

    @OneToOne(mappedBy = "profile")
    private User user;

    public Profile() {
        this.profileID = UUID.randomUUID().toString();
        this.nameAlias = "";
        this.profileImg = null;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public String getNameAlias() {
        return nameAlias;
    }

    public void setNameAlias(String nameAlias) {
        this.nameAlias = nameAlias;
    }

    public byte[] getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(byte[] profileImg) {
        this.profileImg = profileImg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
