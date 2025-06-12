package com.example.model;

public class ScoreInfoResponse {

    private String gameDataID;
    private boolean gameWon;
    private int score;

    public ScoreInfoResponse() {
    }

    public String getGameDataID() {
        return gameDataID;
    }

    public void setGameDataID(String gameDataID) {
        this.gameDataID = gameDataID;
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
