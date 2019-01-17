package com.sap.uncolor.quiz.main_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.models.Quiz;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

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
        Quiz quiz = new Quiz();
        startActivity(QuizActivity.getInstance(this, quiz));
    }
}
