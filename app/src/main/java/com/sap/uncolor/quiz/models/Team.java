package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Team implements ItemModel, Serializable {

    public static final int TYPE = 4;

    private int id;
    private String name;
    private ArrayList<PrivateGamePlayer> privateGamePlayers = new ArrayList<>();
    private int currentPlayer;

    public Team() {
        currentPlayer = 0;
    }

    public Team(String name, ArrayList<PrivateGamePlayer> privateGamePlayers) {
        this.name = name;
        this.privateGamePlayers = privateGamePlayers;
        this.currentPlayer = 0;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public String getName() {
        return name;
    }

    public ArrayList<PrivateGamePlayer> getPrivateGamePlayers() {
        return privateGamePlayers;
    }

    public int getTeamPoints(){
        int counter = 0;
        for (int i = 0; i < privateGamePlayers.size(); i++) {
            counter += privateGamePlayers.get(i).getPointsRight();
        }
        return counter;
    }

    public boolean isWinner(){
        int points = getTeamPoints();
        if(points >= PrivateGame.WIN_POINTS_LIMIT){
            return true;
        }
        return false;
    }

    public PrivateGamePlayer getPlayer(int index){
        return privateGamePlayers.get(index);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivateGamePlayers(ArrayList<PrivateGamePlayer> privateGamePlayers) {
        this.privateGamePlayers = privateGamePlayers;
    }

    public int getId() {
        return id;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void nextPlayer(){
        int teamsCount = privateGamePlayers.size();
        if(teamsCount == 0){
            return;
        }
        if(currentPlayer >= teamsCount - 1){
            currentPlayer = 0;
        }
        else {
            currentPlayer++;
        }
    }
}
