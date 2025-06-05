package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "gamedata")
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


    public String getGameDataID() {
        return gameDataID;
    }

    public void setGameDataID(String gameDataID) {
        this.gameDataID = gameDataID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ScoreInfo getScoreInfo() {
        return scoreInfo;
    }

    public void setScoreInfo(ScoreInfo scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public boolean isGameCleared() {
        return isGameCleared;
    }

    public void setGameCleared(boolean gameCleared) {
        isGameCleared = gameCleared;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
