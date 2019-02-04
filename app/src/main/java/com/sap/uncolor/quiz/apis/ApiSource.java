package com.sap.uncolor.quiz.apis;

import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.TopsModel;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.models.request_datas.AnswerOnQuestionInRoomRequestData;
import com.sap.uncolor.quiz.models.request_datas.CheckAnswerForOfflineRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetQuestionsRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomsRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetTopRequestData;
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
    Call<ResponseModel<Boolean>> checkAnswer(@Body CheckAnswerForOfflineRequestData checkAnswerForOfflineRequestData);

    @Headers("Content-Type: application/json")
    @POST("top")
    Call<ResponseModel<TopsModel>> getTop(@Body GetTopRequestData getTopRequestData);

    @Headers("Content-Type: application/json")
    @POST("getRooms")
    Call<ResponseModel<List<Room>>> getRooms(@Body GetRoomsRequestData getRoomsRequestData);

    @Headers("Content-Type: application/json")
    @POST("getRoom")
    Call<ResponseModel<Room>> getRoom(@Body GetRoomRequestData getRoomRequestData);

    @Headers("Content-Type: application/json")
    @POST("answerOnQuestionInRoom")
    Call<ResponseModel<Boolean>> answerOnQuestionInRoom(@Body AnswerOnQuestionInRoomRequestData answerOnQuestionInRoomRequestData);


}