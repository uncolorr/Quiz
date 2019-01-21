package com.sap.uncolor.quiz.results_activity;

import android.content.Context;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.models.QuestionsResponse;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.utils.QuizParser;

import java.io.IOException;

public class ResultActivityPresenter implements ResultActivityContract.Presenter,
        ApiResponse.ApiFailureListener {

    private Context context;
    private ResultActivityContract.View view;

    public ResultActivityPresenter(Context context, ResultActivityContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void onStartGame() {
        view.showProcessingDialog();
        Api.getSource().getQuestions()
                .enqueue(ApiResponse.getCallback(getApiResponseListener(), this));
    }

    private ApiResponse.ApiResponseListener<QuestionsResponse> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<QuestionsResponse>() {
            @Override
            public void onResponse(QuestionsResponse result) throws IOException {
                view.hideProcessingDialog();
                if(result == null){
                    view.showErrorMessage();
                }
                else {
                    Quiz quiz = QuizParser.toQuizModel(result.getResponse());
                    view.startGame(quiz);
                }
            }
        };
    }

    @Override
    public void onFailure(int code, String message) {
        view.showErrorMessage();
    }
}
