package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class CheckAnswerRequestData {

    private String token;

    public CheckAnswerRequestData() {
        this.token = App.getToken();
    }
}
