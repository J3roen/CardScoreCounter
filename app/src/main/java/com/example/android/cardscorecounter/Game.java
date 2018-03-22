package com.example.android.cardscorecounter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Jeroen on 19-3-2018.
 */

public class Game implements Serializable{
    private int currentRound;
    private ArrayList<Player> playerArrayList;

    public Game() {
        playerArrayList = new ArrayList<>();
        setRound(1);
    }

    private void setRound(int round) {
        this.currentRound = round;
    }

    public void setNextRound() {
        setRound(this.currentRound + 1);
    }

    public void addPlayer(String name) {
        playerArrayList.add(new Player(name));
    }

    public int getPlayerCount() {
        return playerArrayList.size();
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public ArrayList<Player> getPlayerArrayList() {
        return this.playerArrayList;
    }
}
