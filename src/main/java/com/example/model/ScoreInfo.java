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
}
