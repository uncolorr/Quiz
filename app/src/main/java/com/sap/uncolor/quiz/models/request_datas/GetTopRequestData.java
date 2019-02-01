package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GetTopRequestData {
    private String token;

    public GetTopRequestData() {
        this.token = App.getToken();
    }
}
