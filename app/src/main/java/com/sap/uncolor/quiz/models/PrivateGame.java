package com.sap.uncolor.quiz.models;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class PrivateGame {

    public static final String KEY_PRIVATE_GAME = "private_game";

    private ArrayList<Team> teams;
    private int currentTeam;

    private static PrivateGame privateGame;


    private PrivateGame(ArrayList<Team> teams) {
        if(privateGame == null){
            privateGame = new PrivateGame(teams);
        }
        this.teams = teams;
        this.currentTeam = 0;
    }

    public void nextTeam(){
        int teamsCount = teams.size();
        if(teamsCount == 0){
            return;
        }
        if(currentTeam >= teamsCount - 1){
            currentTeam = 0;
        }
        else {
            currentTeam++;
        }
    }

    public int getCurrentTeamIndex() {
        return currentTeam;
    }

    public int getCurrentPlayerIndex(){
        return teams.get(currentTeam).getCurrentPlayer();
    }

    public void nextPlayer(){
        teams.get(currentTeam).nextPlayer();
    }

    public PrivateGamePlayer getCurrentPlayer(){
        return teams.get(currentTeam).getPrivateGamePlayers().get(getCurrentPlayerIndex());
    }

    public Team getCurrentTeam(){
        return teams.get(currentTeam);
    }

    public void save(){
        Hawk.put(KEY_PRIVATE_GAME, this);
    }


}
