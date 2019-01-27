package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

public class PrivateGamePlayer implements ItemModel {

    public static final int TYPE = 3;

    private String name;

    private int pointsRight;

    private int pointsWrong;

    public PrivateGamePlayer(String name) {
        this.name = name;
        pointsRight = 0;
        pointsWrong = 0;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public String getName() {
        return name;
    }

    public int getPointsRight() {
        return pointsRight;
    }

    public int getPointsWrong() {
        return pointsWrong;
    }

    public void addPointsRight(){
        pointsRight++;
    }

    public void addPointsWrong(){
        pointsWrong++;
    }




}
