package com.sap.uncolor.quiz.apis;

import com.sap.uncolor.quiz.models.AuthResponse;
import com.sap.uncolor.quiz.models.QuestionsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiSource {

    @GET("getQuestions")
    Call<QuestionsResponse> getQuestions();


    @FormUrlEncoded
    @POST("login")
    Call<AuthResponse> login(@Field("login")String login,
                             @Field("password")String password);

    @FormUrlEncoded
    @POST("register")
    Call<AuthResponse> register(@Field("login") String login,
                                @Field("password") String password,
                                @Field("sex") String sex);

}
