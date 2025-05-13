package com.example.model;

import jakarta.persistence.*;

@Entity
public class GameData {
    @Id
    private String gameDataID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    @OneToOne
    @JoinColumn(name = "scoreInfoID")
    private ScoreInfo scoreInfo;

    private boolean isGameCleared;
    private String occupation;

    // Getters and Setters
}
