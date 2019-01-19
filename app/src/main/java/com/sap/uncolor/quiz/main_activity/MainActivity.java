package com.sap.uncolor.quiz.main_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.TopActivity;
import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.QuestionsResponse;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.utils.MessageReporter;
import com.sap.uncolor.quiz.utils.QuizParser;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener {

    @BindView(R.id.buttonSingleGame)
    Button buttonSingleGame;

    private AlertDialog loadingDialog;


    public static Intent getInstance(Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.buttonSingleGame)
    void onButtonSingleGameClick(){
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        loadingDialog.show();
        Api.getSource().getQuestions()
                .enqueue(ApiResponse.getCallback(getApiResponseListener(), this));
    }

    @OnClick(R.id.imageButtonSettings)
    void onSettingsButtonClick(){

    }

    @OnClick(R.id.imageButtonTop)
    void onTopButtonClick(){
        startActivity(TopActivity.getInstance(this));
    }

    @OnClick(R.id.imageButtonExit)
    void onExitButtonClick(){

    }

    private ApiResponse.ApiResponseListener<QuestionsResponse> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<QuestionsResponse>() {
            @Override
            public void onResponse(QuestionsResponse result) throws IOException {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка создания игры");
                }
                else {
                     Quiz quiz = QuizParser.toQuizModel(result.getResponse());
                     startActivity(QuizActivity.getInstance(MainActivity.this, quiz));
                }
            }
        };
    }

    public void cancelLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.cancel();
        }
    }

    @Override
    public void onFailure(int code, String message) {
        App.Log("onFailure");
        cancelLoadingDialog();
        MessageReporter.showMessage(MainActivity.this,
                "Ошибка",
                "Ошибка создания игры");
    }
}
