package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GetUserByIdRequestData {

    private int user_id;
    private String token;

    public GetUserByIdRequestData() {
        this.token = App.getUser().getToken();
        this.user_id = App.getUser().getId();
    }
}
