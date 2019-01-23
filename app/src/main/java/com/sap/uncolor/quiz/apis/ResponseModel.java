package com.sap.uncolor.quiz.apis;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseModel<T> {

    @SerializedName("errorCode")
    @Expose
    private int errorCode;

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    @SerializedName("result")
    @Expose
    private T result;

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getResult() {
        return result;
    }
}