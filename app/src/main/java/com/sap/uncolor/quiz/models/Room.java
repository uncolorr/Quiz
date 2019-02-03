package com.sap.uncolor.quiz.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sap.uncolor.quiz.ItemModel;

import java.io.Serializable;

public class Room implements ItemModel, Serializable {

    public static final int TYPE = 5;

    @Override
    public int getType() {
        return TYPE;
    }

    @SerializedName("creator")
    private User creator;

    // might be null
    @SerializedName("competitor")
    private User competitor;

    //might be null
    @Nullable
    @SerializedName("rounds")
    private String rounds;


    @SerializedName("is_booked")
    private boolean isBooked;

    public User getCreator() {
        return creator;
    }

    public User getCompetitor() {
        return competitor;
    }

    @Nullable
    public String getRounds() {
        return rounds;
    }

    public boolean isBooked() {
        return isBooked;
    }
}
