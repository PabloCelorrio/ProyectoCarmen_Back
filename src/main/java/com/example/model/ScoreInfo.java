package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "scoreinfo")
public class ScoreInfo {
    @Id
    private String scoreInfoID;
    private String playerRank;
    private boolean gameWon;
    private int score;

    // Getters and Setters

    public ScoreInfo() {
        this.score = 0;
        this.gameWon = false;
        this.playerRank = "Rookie";
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
}
