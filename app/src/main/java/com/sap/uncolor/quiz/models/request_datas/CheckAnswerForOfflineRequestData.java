package com.sap.uncolor.quiz.models.request_datas;

import com.sap.uncolor.quiz.application.App;

public class CheckAnswerForOfflineRequestData {

    private String token;
    private int question_id;
    private String answer;

    public CheckAnswerForOfflineRequestData(int question_id, String answer) {
        this.token = App.getToken();
        this.question_id = question_id;
        this.answer = answer;
    }
}
