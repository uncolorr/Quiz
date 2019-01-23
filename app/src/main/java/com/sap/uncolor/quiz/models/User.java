package com.sap.uncolor.quiz.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.sap.uncolor.quiz.ItemModel;

public class User implements ItemModel{

    public static final int TYPE = 2;

    public static final String SEX_TYPE_MALE = "м";
    public static final String SEX_TYPE_FEMALE = "ж";

    @Nullable
    @SerializedName("id")
    private int id;

    @Nullable
    @SerializedName("login")
    private String login;

    @Nullable
    @SerializedName("sex")
    private String sex;

    @Nullable
    @SerializedName("token")
    private String token;

    @Nullable
    @SerializedName("points")
    private int points;

    @Nullable
    @SerializedName("error")
    private String error;

    @Nullable
    public int getId() {
        return id;
    }

    @Nullable
    public String getLogin() {
        return login;
    }

    @Nullable
    public String getSex() {
        return sex;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Nullable
    public String getError() {
        return error;
    }

    @Nullable
    public int getPoints() {
        return points;
    }

    @Override
    public int getType() {
        return TYPE;
    }
}
