package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopsModel {
    @SerializedName("place")
    private int place;

    @SerializedName("users")
    private List<User> users;


    public int getPlace() {
        return place;
    }

    public List<User> getUsers() {
        return users;
    }
}
