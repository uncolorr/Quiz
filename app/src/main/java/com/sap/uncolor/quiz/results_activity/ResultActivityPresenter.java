package com.sap.uncolor.quiz.results_activity;

import android.content.Context;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.request_datas.GetQuestionsRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomByIdRequestData;

import java.util.List;

public class ResultActivityPresenter implements ResultActivityContract.Presenter,
        ApiResponse.ApiFailureListener {

    private Context context;
    private ResultActivityContract.View view;

    public ResultActivityPresenter(Context context, ResultActivityContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void onStartSingleGame() {
        view.showProcessingDialog();
        Api.getSource().getQuestions(new GetQuestionsRequestData())
                .enqueue(ApiResponse.getCallback(getApiResponseListener(), this));
    }

    @Override
    public void onStartOnlineGame() {
        view.startOnlineGame();
    }

    @Override
    public void onUpdateInfoAboutOnlineGame(Room room) {
        Api.getSource().getRoomByUUID(new GetRoomByIdRequestData(room.getUuid()))
                .enqueue(ApiResponse.getCallback(getInfoAboutOnlineGame(), this));
    }

    private ApiResponse.ApiResponseListener<ResponseModel<Room>> getInfoAboutOnlineGame() {
        return new ApiResponse.ApiResponseListener<ResponseModel<Room>>() {
            @Override
            public void onResponse(ResponseModel<Room> result) {
                if(result == null || result.getResult() == null){
                    view.showErrorMessage();
                }
                else {
                    Room room = result.getResult();
                    view.updateInfoAboutOnlineGame(room);
                }
            }
        };
    }

    private ApiResponse.ApiResponseListener<ResponseModel<List<Question>>> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<List<Question>>>() {
            @Override
            public void onResponse(ResponseModel<List<Question>> result) {
                view.hideProcessingDialog();
                if(result == null){
                    view.showErrorMessage();
                }
                else {
                    Quiz quiz = new Quiz();
                    quiz.setQuestions(result.getResult());
                    view.startSingleGame(quiz);
                }
            }
        };
    }

    @Override
    public void onFailure(int code, String message) {
        view.showErrorMessage();
    }
}
