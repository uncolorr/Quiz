package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Round {

    @SerializedName("id")
    private int id;

    @SerializedName("questions")
    private List<Question> questions;

    @SerializedName("creator_mask")
    private int creatorMask;

    @SerializedName("competitor_mask")
    private int competitorMask;

    @SerializedName("creatorLastQuestion")
    private int creatorLastQuestion;

    @SerializedName("competitorLastQuestion")
    private int competitorLastQuestion;


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
}

