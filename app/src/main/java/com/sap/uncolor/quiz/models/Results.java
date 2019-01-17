package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

import java.util.ArrayList;

public class Results implements ItemModel {

    public static final int TYPE = 1;

    private ArrayList<Integer> answers;

    @Override
    public int getType() {
        return TYPE;
    }

    public Results(){

    }

    public Results(ArrayList<Integer> answers){
        this.answers = answers;
    }


    public ArrayList<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<Integer> answers) {
        this.answers = answers;
    }
}
