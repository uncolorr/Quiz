package com.sap.uncolor.quiz;

import java.io.Serializable;

public class Question implements Serializable{

    private String question;
    private String variant1;
    private String variant2;
    private String variant3;
    private String variant4;
    private int variantRight;

    public Question() {
        question = "Вопрос 1";
        variant1 = "Вариант 1";
        variant2 = "Вариант 2";
        variant3 = "Вариант 3";
        variant4 = "Вариант 4";
        variantRight = 2;
    }

    public String getQuestion() {
        return question;
    }

    public String getVariant1() {
        return variant1;
    }

    public String getVariant2() {
        return variant2;
    }

    public String getVariant3() {
        return variant3;
    }

    public String getVariant4() {
        return variant4;
    }

    public int getVariantRight() {
        return variantRight;
    }

}
