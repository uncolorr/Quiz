package com.sap.uncolor.quiz.quiz_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sap.uncolor.quiz.AnswerListener;
import com.sap.uncolor.quiz.QuizFragmentPagerAdapter;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.results_activity.ResultsActivity;
import com.sap.uncolor.quiz.widgets.AnimatingProgressBar;
import com.sap.uncolor.quiz.widgets.NonSwipeViewPager;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {

    private static final String ARG_QUIZ = "quiz";

    private static final int TIME_INTERVAL = 1000;
    private static final int TIME_FOR_ANSWER = 10000;

    @BindView(R.id.viewPagerQuiz)
    NonSwipeViewPager viewPager;

    @BindView(R.id.textViewRoundNumber)
    TextView textViewRoundNumber;

    @BindView(R.id.progressBarTimer)
    AnimatingProgressBar progressBarTimer;

    private QuizFragmentPagerAdapter fragmentPagerAdapter;

    private Quiz quiz;

    private CountDownTimer countDownTimer;

    private ArrayList<Integer> answers = new ArrayList<>();

    public static Intent getInstance(Context context, Quiz quiz){
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(ARG_QUIZ, quiz);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        quiz = (Quiz) getIntent().getSerializableExtra(ARG_QUIZ);
        fragmentPagerAdapter = new QuizFragmentPagerAdapter(getSupportFragmentManager(), quiz);
        fragmentPagerAdapter.setAnswerListener(getAnswerListener());
        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(fragmentPagerAdapter);
        textViewRoundNumber.setText("Раунд 1");
        startTimer();
    }

    private void startTimer() {
        progressBarTimer.setProgress(10);
        countDownTimer = new CountDownTimer(TIME_FOR_ANSWER, TIME_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                int value = (int) millisUntilFinished / TIME_INTERVAL;
                App.Log("value: " + value);
                progressBarTimer.setProgress(value);
            }

            public void onFinish() {
                progressBarTimer.setProgress(0);
                answers.add(0);
                swipeQuestion();
            }

        }.start();
    }

    private ArrayList<Integer> generateComputerAnswers(){
        ArrayList<Integer> computerAnswers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            int value = random.nextInt(2);
            computerAnswers.add(value);
        }
        return computerAnswers;
    }


    private AnswerListener getAnswerListener() {
        return new AnswerListener() {
            @Override
            public void onQuestionAnswered(boolean isAnswerRight, int round) {
                if(isAnswerRight) {
                    answers.add(1);
                }
                else {
                    answers.add(0);
                }
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        swipeQuestion();
                    }

                }.start();
            }
        };
    }

    private void swipeQuestion(){
        int currentPosition = viewPager.getCurrentItem();
        if(currentPosition == 0){
            viewPager.setCurrentItem(1, true);
            textViewRoundNumber.setText("Раунд 2");
            startTimer();
        }
        if(currentPosition == 1){
            viewPager.setCurrentItem(2, true);
            textViewRoundNumber.setText("Раунд 3");
            startTimer();
        }
        if(currentPosition == 2){
            finish();
            ArrayList<Integer> enemyAnswers = generateComputerAnswers();
            startActivity(ResultsActivity.getInstanceForSingleGame(QuizActivity.this, answers, enemyAnswers));
        }
    }
}
