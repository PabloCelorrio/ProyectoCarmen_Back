package com.example.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "gamedata")
public class GameData {
    @Id
    private String gameDataID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    private boolean isGameCleared;
    private String occupation;
    private int wins;
    private int loses;

    // Getters and Setters


    public GameData() {
        this.gameDataID = UUID.randomUUID().toString();
        this.occupation = "Rookie";
        this.wins = 0;
        this.loses = 0;
    }

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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }
}
