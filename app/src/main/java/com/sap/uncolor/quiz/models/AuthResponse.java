package com.sap.uncolor.quiz.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("response")
    User response;

    public User getResponse() {
        return response;
    }
}
