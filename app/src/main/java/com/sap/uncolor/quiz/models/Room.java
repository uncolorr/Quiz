package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;
import com.sap.uncolor.quiz.ItemModel;

import java.io.Serializable;
import java.util.List;

public class Room implements ItemModel, Serializable {

    public static final int TYPE = 5;

    @Override
    public int getType() {
        return TYPE;
    }

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("creator")
    private User creator;

    // might be null
    @SerializedName("competitor")
    private User competitor;

    @SerializedName("rounds")
    private List<Round> rounds;

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
        if(round.getCreatorLastQuestion() != 0){
            return null;
        }
        return round.getQuestions();
    }
}
