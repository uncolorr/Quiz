package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;

public class QuestionsResponse {

    @SerializedName("response")
    Object[][] response;

    public Object[][] getResponse() {
        return response;
    }

}
