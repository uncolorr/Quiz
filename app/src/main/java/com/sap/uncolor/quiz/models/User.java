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

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("wins_count")
    private int winsCount;

    @SerializedName("loses_count")
    private int losesCount;


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

    public String getAvatar() {
        return avatar;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getWinsCount() {
        return winsCount;
    }

    public void setWinsCount(int winsCount) {
        this.winsCount = winsCount;
    }

    public int getLosesCount() {
        return losesCount;
    }

    public void setLosesCount(int losesCount) {
        this.losesCount = losesCount;
    }
}
