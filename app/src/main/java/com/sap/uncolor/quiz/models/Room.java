package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;
import com.sap.uncolor.quiz.ItemModel;
import com.sap.uncolor.quiz.application.App;

import java.io.Serializable;
import java.util.List;

public class Room implements ItemModel, Serializable {

    public static final int TYPE = 5;

    public static final int WINNER_NONE = 0;
    public static final int WINNER_DRAW_ID = -1;

    @Override
    public int getType() {
        return TYPE;
    }

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("creator")
    private User creator;

    @SerializedName("competitor")
    private User competitor;

    @SerializedName("rounds")
    private List<Round> rounds;

    @SerializedName("winner_id")
    private int winnerId;

    @SerializedName("is_booked")
    private boolean isBooked;

    public User getCreator() {
        return creator;
    }

    public User getCompetitor() {
        return competitor;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public String getUuid() {
        return uuid;
    }

    public List<Question> getLastNotAnsweredQuestions(){
        if(rounds == null){
            return null;
        }
        if(rounds.size() == 0){
            return null;
        }
        Round round = rounds.get(rounds.size() - 1);
        return round.getQuestions();
    }

    public boolean isMine() {
        if(getCompetitor() == null){
            return true;
        }
        if(getCreator().getId() == App.getUser().getId()){
            return true;
        }
        if(getCompetitor().getId() == App.getUser().getId()){
            return false;
        }
        return false;
    }

    public int getWinnerId() {
        return winnerId;
    }
}
