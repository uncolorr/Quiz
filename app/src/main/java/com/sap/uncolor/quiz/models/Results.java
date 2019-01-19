package com.sap.uncolor.quiz.models;

import com.sap.uncolor.quiz.ItemModel;

import java.util.ArrayList;

public class Results implements ItemModel {

    public static final int TYPE = 1;

    private ArrayList<Integer> myAnswers;

    private ArrayList<Integer> enemyAnswers;

    @Override
    public int getType() {
        return TYPE;
    }

    public Results(){

    }

    public Results(ArrayList<Integer> answers){
        this.myAnswers = answers;
    }


    public ArrayList<Integer> getMyAnswers() {
        return myAnswers;
    }

    public void setMyAnswers(ArrayList<Integer> myAnswers) {
        this.myAnswers = myAnswers;
    }

    public int getMyRightAnswersCount(){
        int counter = 0;
        for (int i = 0; i < myAnswers.size(); i++) {
            if(myAnswers.get(i) > 0){
                counter++;
            }
        }
        return counter;
    }

    public int getEnemyRightAnswersCount(){
        int counter = 0;
        for (int i = 0; i < enemyAnswers.size(); i++) {
            if(enemyAnswers.get(i) > 0){
                counter++;
            }
        }
        return counter;
    }

    public ArrayList<Integer> getEnemyAnswers() {
        return enemyAnswers;
    }

    public void setEnemyAnswers(ArrayList<Integer> enemyAnswers) {
        this.enemyAnswers = enemyAnswers;
    }
}
