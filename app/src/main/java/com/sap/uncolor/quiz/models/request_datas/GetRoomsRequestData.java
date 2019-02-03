package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GetRoomsRequestData {
    private String token;

    public GetRoomsRequestData() {
        this.token = App.getToken();
    }
}
