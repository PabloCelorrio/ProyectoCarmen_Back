package com.example.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "scoreinfo")
public class ScoreInfo {
    @Id
    private String scoreInfoID;
    private String playerRank;
    private boolean gameWon;
    private int score;

    @OneToOne
    @JoinColumn(name = "gameDataID", referencedColumnName = "gameDataID")
    private GameData gameData;

    // Getters and Setters

    public ScoreInfo() {
    }

    public ScoreInfo(String playerRank, boolean gameWon, int score, GameData gameData) {
        this.scoreInfoID = UUID.randomUUID().toString();
        this.playerRank = playerRank;
        this.gameWon = gameWon;
        this.score = score;
        this.gameData = gameData;
    }

    public String getScoreInfoID() {
        return scoreInfoID;
    }

    public void setScoreInfoID(String scoreInfoID) {
        this.scoreInfoID = scoreInfoID;
    }

    public String getPlayerRank() {
        return playerRank;
    }

    public void setPlayerRank(String playerRank) {
        this.playerRank = playerRank;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameData getGameData() {
        return gameData;
    }

    public void setGameDataId(GameData gameDataId) {
        this.gameData = gameDataId;
    }
}
