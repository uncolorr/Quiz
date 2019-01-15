package com.sap.uncolor.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

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
