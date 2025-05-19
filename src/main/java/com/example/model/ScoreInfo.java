package com.example.model;

import javax.persistence.*;

@Entity
public class ScoreInfo {
    @Id
    private String scoreInfoID;
    private int wins;
    private int loses;
    private int bestScore;

    // Getters and Setters

    public ScoreInfo() {
        this.bestScore = 0;
        this.loses = 0;
        this.wins = 0;
    }

    public String getScoreInfoID() {
        return scoreInfoID;
    }

    public void setScoreInfoID(String scoreInfoID) {
        this.scoreInfoID = scoreInfoID;
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

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
}
