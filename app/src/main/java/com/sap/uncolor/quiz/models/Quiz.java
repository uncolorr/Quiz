package com.sap.uncolor.quiz.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable{

    public static final int VARIANT_ONE = 1;
    public static final int VARIANT_TWO = 2;
    public static final int VARIANT_THREE = 3;
    public static final int VARIANT_FOUR = 4;

    private List<Question> questions = new ArrayList<>();


    public Quiz() {

    }

    public Question getQuestion(int round) {
        return questions.get(round - 1);
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public void add(Question question){
        questions.add(question);
    }

}
