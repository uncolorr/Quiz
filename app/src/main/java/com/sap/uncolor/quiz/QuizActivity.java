package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizActivity extends AppCompatActivity {

    private static final String ARG_QUIZ = "quiz";

    @BindView(R.id.viewPagerQuiz)
    NonSwipeViewPager viewPager;

    @BindView(R.id.textViewRoundNumber)
    TextView textViewRoundNumber;

    @BindView(R.id.progressBarTimer)
    ProgressBar progressBarTimer;

    private QuizFragmentPagerAdapter fragmentPagerAdapter;

    private Quiz quiz;

    private CountDownTimer countDownTimer;

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
        countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                progressBarTimer.setProgress((int) millisUntilFinished / 1000);
            }

            public void onFinish() {
                swipeQuestion();
            }

        }.start();
    }


    private AnswerListener getAnswerListener() {
        return new AnswerListener() {
            @Override
            public void onQuestionAnswered(int variant, int round) {
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }
                new CountDownTimer(1500, 1500) {

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
            startActivity(ResultsActivity.getInstance(QuizActivity.this));
        }
    }
}
