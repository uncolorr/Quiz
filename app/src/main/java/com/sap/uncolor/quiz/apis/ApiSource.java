package com.sap.uncolor.quiz.apis;

import com.sap.uncolor.quiz.models.QuestionsResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiSource {

    @GET("getQuestions")
    Call<QuestionsResponse> getMusic();
}
