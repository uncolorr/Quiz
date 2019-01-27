package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GetQuestionsRequestData {

    private String token;

    public GetQuestionsRequestData() {
        this.token = App.getToken();
    }
}
