package com.sap.uncolor.quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable{

    public static final int VARIANT_ONE = 1;
    public static final int VARIANT_TWO = 2;
    public static final int VARIANT_THREE = 3;
    public static final int VARIANT_FOUR = 4;

    private List<Question> questions = new ArrayList<>();

    private List<Boolean> answers = new ArrayList<>();

    public Quiz() {
        questions.add(new Question());
        questions.add(new Question());
        questions.add(new Question());
    }

    public Question getQuestion(int round) {
        return questions.get(round - 1);
    }

}
