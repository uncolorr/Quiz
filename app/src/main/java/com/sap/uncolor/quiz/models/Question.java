package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Question implements Serializable{

    @SerializedName("id")
    private int id;

    @SerializedName("question")
    private String question;

    @SerializedName("variant1")
    private String variant1;

    @SerializedName("variant2")
    private String variant2;

    @SerializedName("variant3")
    private String variant3;

    @SerializedName("variant4")
    private String variant4;


    public Question() {

    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setVariant1(String variant1) {
        this.variant1 = variant1;
    }

    public void setVariant2(String variant2) {
        this.variant2 = variant2;
    }

    public void setVariant3(String variant3) {
        this.variant3 = variant3;
    }

    public void setVariant4(String variant4) {
        this.variant4 = variant4;
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

    public int getId() {
        return id;
    }
}
