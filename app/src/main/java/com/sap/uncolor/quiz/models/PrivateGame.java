package com.sap.uncolor.quiz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PrivateGame implements Serializable{

    private ArrayList<Team> teams;
    private int currentTeam;

    public PrivateGame(ArrayList<Team> teams) {
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

    public void addResultsToCurrentPlayer(ArrayList<Integer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            if(answers.get(i) == 1){
                getCurrentPlayer().addPointsRight();
            }
            else {
                getCurrentPlayer().addPointsWrong();
            }
        }
    }
}
