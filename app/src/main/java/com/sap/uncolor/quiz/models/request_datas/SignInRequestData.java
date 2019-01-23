package com.sap.uncolor.quiz.models.request_datas;

public class SignInRequestData {

    private String login;
    private String password;

    public SignInRequestData(String login, String password){
        this.login = login;
        this.password = password;
    }
}
