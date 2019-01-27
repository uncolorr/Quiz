package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

public class Player implements ItemModel {

    public static final int TYPE = 3;

    private String name;

    public Player(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public String getName() {
        return name;
    }
}
