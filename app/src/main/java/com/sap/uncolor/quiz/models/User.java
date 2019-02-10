package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;
import com.sap.uncolor.quiz.ItemModel;

import java.io.Serializable;

public class User implements ItemModel, Serializable {

    public static final int TYPE = 2;

    public static final String SEX_TYPE_MALE = "м";
    public static final String SEX_TYPE_FEMALE = "ж";

    @SerializedName("id")
    private int id;

    @SerializedName("login")
    private String login;

    @SerializedName("sex")
    private String sex;

    @SerializedName("token")
    private String token;

    @SerializedName("points")
    private int points;


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSex() {
        return sex;
    }

    public String getToken() {
        return token;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
