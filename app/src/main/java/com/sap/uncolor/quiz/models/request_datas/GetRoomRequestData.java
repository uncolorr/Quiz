package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GetRoomRequestData {

    private String token;

    public GetRoomRequestData() {
        this.token = App.getToken();
    }
}
