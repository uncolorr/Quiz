package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class AnswerOnQuestionInRoomRequestData {

    private String token;
    private int question_index;
    private String answer;
    private int round_index;
    private String room_uuid;

    public AnswerOnQuestionInRoomRequestData(int question_index, String answer, int round_index, String room_uuid) {
        this.token = App.getToken();
        this.question_index = question_index;
        this.answer = answer;
        this.room_uuid = room_uuid;
        this.round_index = round_index;
    }
}
