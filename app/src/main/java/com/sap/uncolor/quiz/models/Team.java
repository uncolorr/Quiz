package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

import java.util.ArrayList;

public class Team implements ItemModel{

    public static final int TYPE = 4;

    private String name;
    private ArrayList<Player> players;

    public Team(String name, ArrayList<Player> players) {
        this.name = name;
        this.players = players;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}
