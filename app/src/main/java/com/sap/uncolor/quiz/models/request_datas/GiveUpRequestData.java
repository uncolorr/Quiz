package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GiveUpRequestData {

    private String token;
    private String room_uuid;

    public GiveUpRequestData(String room_uuid) {
        this.token = App.getUser().getToken();
        this.room_uuid = room_uuid;
    }
}
