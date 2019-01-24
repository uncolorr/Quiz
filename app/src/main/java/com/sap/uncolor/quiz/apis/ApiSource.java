package com.sap.uncolor.quiz.apis;

import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.models.request_datas.GetQuestionsRequestData;
import com.sap.uncolor.quiz.models.request_datas.SignInRequestData;
import com.sap.uncolor.quiz.models.request_datas.SignUpRequestData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiSource {

    @Headers("Content-Type: application/json")
    @POST("register")
    Call<ResponseModel<User>> register(@Body SignUpRequestData signUpRequestData);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<ResponseModel<User>> login(@Body SignInRequestData signInRequestData);

    @Headers("Content-Type: application/json")
    @POST("getQuestions")
    Call<ResponseModel<List<Question>>> getQuestions(@Body GetQuestionsRequestData getQuestionsRequestData);

    @Headers("Content-Type: application/json")
    @POST("checkAnswer")
    Call<ResponseModel<List<Question>>> checkAnswer(@Body GetQuestionsRequestData getQuestionsRequestData);

    /*@FormUrlEncoded
    @POST("login")
    Call<AuthResponse> login(@Field("login")String login,
                             @Field("password")String password);

    @FormUrlEncoded
    @POST("register")
    Call<AuthResponse> register(@Field("login") String login,
                                @Field("password") String password,
                                @Field("sex") String sex);

    @FormUrlEncoded
    @POST("updatePoints")
    Call<AuthResponse> updatePoints(@Field("token") String token, @Field("points") int points);*/



}
