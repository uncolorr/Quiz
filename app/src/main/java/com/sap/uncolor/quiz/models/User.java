package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

public class User implements ItemModel{

    public static final int TYPE = 2;

    @Override
    public int getType() {
        return TYPE;
    }
}
