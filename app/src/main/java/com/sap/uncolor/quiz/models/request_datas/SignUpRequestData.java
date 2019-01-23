package com.sap.uncolor.quiz.models.request_datas;

public class SignUpRequestData {

    private String login;
    private String password;
    private String sex;

    public SignUpRequestData(String login,
                             String password,
                             String sex){
        this.login = login;
        this.password = password;
        this.sex = sex;
    }
}
