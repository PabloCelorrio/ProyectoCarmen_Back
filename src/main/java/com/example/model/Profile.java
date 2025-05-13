package com.example.model;

import javax.persistence.*;

@Entity
public class Profile {
    @Id
    private String profileID;
    private String nameAlias;

    @Lob
    private byte[] profileImg;

    // Getters and Setters
}
