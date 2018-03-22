package com.example.android.cardscorecounter;

import java.io.Serializable;

/**
 * Created by Jeroen on 19-3-2018.
 */

public class Player implements Serializable{
    private int score;
    private String name;

    public Player() {
        setScore(0);
        setName("John Doe");
    }
    public Player(String name) {
        setScore(0);
        setName(name);
    }

    public void subtractScore(int score) {
        this.score -= score;
    }

    public int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
