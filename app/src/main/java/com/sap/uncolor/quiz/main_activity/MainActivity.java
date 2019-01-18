package com.sap.uncolor.quiz.main_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.QuestionsResponse;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.utils.QuizParser;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener {

    @BindView(R.id.buttonSingleGame)
    Button buttonSingleGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.buttonSingleGame)
    void onButtonSingleGameClick(){
        Api.getSource().getMusic().enqueue(ApiResponse.getCallback(getApiResponseListener(),
                this));
    }

    private ApiResponse.ApiResponseListener<QuestionsResponse> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<QuestionsResponse>() {
            @Override
            public void onResponse(QuestionsResponse result) throws IOException {
                if(result == null){
                    App.Log("result null");
                }
                else {
                     Quiz quiz = QuizParser.toQuizModel(result.getResponse());
                     startActivity(QuizActivity.getInstance(MainActivity.this, quiz));
                }
            }
        };
    }



    @Override
    public void onFailure(int code, String message) {
        App.Log("onFailure");
    }
}
