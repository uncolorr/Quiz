package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class GetRoomByIdRequestData {

    private String token;
    private String room_uuid;

    public GetRoomByIdRequestData(String room_uuid) {
        this.token = App.getToken();
        this.room_uuid = room_uuid;
    }


}
