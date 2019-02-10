package com.sap.uncolor.quiz.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sap.uncolor.quiz.ItemModel;

import java.io.Serializable;
import java.util.List;

public class Round implements ItemModel, Serializable {

    public static final int TYPE = 6;

    public static final int STATE_COMPLETED = 1;
    public static final int STATE_NEXT_GAME = 2;


    @SerializedName("id")
    private int id;

    @SerializedName("questions")
    private List<Question> questions;

    @SerializedName("creator_mask")
    private int creatorMask;

    @SerializedName("competitor_mask")
    private int competitorMask;

    @Nullable
    @SerializedName("creator_last_question")
    private int creatorLastQuestion;

    @Nullable
    @SerializedName("competitor_last_question")
    private int competitorLastQuestion;

    private int state;

    private boolean isMine;

    public int getId() {
        return id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getCreatorMask() {
        return creatorMask;
    }

    public int getCompetitorMask() {
        return competitorMask;
    }

    public int getCreatorLastQuestion() {
        return creatorLastQuestion;
    }

    public int getCompetitorLastQuestion() {
        return competitorLastQuestion;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}

