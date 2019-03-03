package com.sap.uncolor.quiz.quiz_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sap.uncolor.quiz.AnswerListener;
import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.PrivateGameResultsActivity;
import com.sap.uncolor.quiz.QuizFragmentPagerAdapter;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.dialogs.MessageReporter;
import com.sap.uncolor.quiz.models.PrivateGame;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.request_datas.AnswerOnQuestionInRoomRequestData;
import com.sap.uncolor.quiz.models.request_datas.GiveUpRequestData;
import com.sap.uncolor.quiz.results_activity.ResultsActivity;
import com.sap.uncolor.quiz.services.GiveUpService;
import com.sap.uncolor.quiz.widgets.AnimatingProgressBar;
import com.sap.uncolor.quiz.widgets.NonSwipeViewPager;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener {

    private static final String ARG_QUIZ = "quiz";
    private static final String ARG_PRIVATE_GAME = "private_game";
    private static final String ARG_MODE = "mode";
    private static final String ARG_ROOM = "room";

    public static final int MODE_SINGLE_GAME = 1;
    public static final int MODE_PRIVATE_GAME = 2;
    public static final int MODE_ONLINE_GAME = 3;

    private static final int TIME_INTERVAL = 1000;
    private static final int TIME_FOR_ANSWER = 15000;

    @BindView(R.id.viewPagerQuiz)
    NonSwipeViewPager viewPager;

    @BindView(R.id.textViewRoundNumber)
    TextView textViewRoundNumber;

    @BindView(R.id.progressBarTimer)
    AnimatingProgressBar progressBarTimer;

    private boolean isOnlineQuizFinished = false;

    private QuizFragmentPagerAdapter fragmentPagerAdapter;

    private Quiz quiz;

    private PrivateGame privateGame;

    private Room room;

    private CountDownTimer countDownTimer;

    private ArrayList<Integer> answers = new ArrayList<>();

    private static int timeLeft = 0;

    private int mode;

    private AlertDialog exitFromGameLoading;


    public static Intent getInstanceForSingleGame(Context context, Quiz quiz) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(ARG_QUIZ, quiz);
        intent.putExtra(ARG_MODE, MODE_SINGLE_GAME);
        return intent;
    }

    public static Intent getInstanceForPrivateGame(Context context, Quiz quiz, PrivateGame privateGame) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(ARG_PRIVATE_GAME, privateGame);
        intent.putExtra(ARG_QUIZ, quiz);
        intent.putExtra(ARG_MODE, MODE_PRIVATE_GAME);
        return intent;
    }

    public static Intent getInstanceForOnlineGame(Context context, Room room) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(ARG_MODE, MODE_ONLINE_GAME);
        intent.putExtra(ARG_ROOM, room);
        return intent;
    }

    public static int getPoints() {
        int points = 0;
        if (timeLeft >= 10) {
            points = 8;
        }

        if (timeLeft >= 8) {
            points = 6;
        }

        if (timeLeft >= 6) {
            points = 4;
        }

        if (timeLeft >= 4) {
            points = 2;
        }

        return points;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.Log("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        ButterKnife.bind(this);
        mode = getIntent().getIntExtra(ARG_MODE, 0);
        quiz = (Quiz) getIntent().getSerializableExtra(ARG_QUIZ);
        privateGame = (PrivateGame) getIntent().getSerializableExtra(ARG_PRIVATE_GAME);
        room = (Room) getIntent().getSerializableExtra(ARG_ROOM);
        exitFromGameLoading = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_EXIT_FROM_GAME);
        if (mode == MODE_ONLINE_GAME) {
            quiz = new Quiz();
            if (room.getLastNotAnsweredQuestions() != null) {
                quiz.setQuestions(room.getLastNotAnsweredQuestions());
            }
            fragmentPagerAdapter = new QuizFragmentPagerAdapter(getSupportFragmentManager(),
                    quiz, room, room.getRounds().size() - 1);
        }
        if (mode == MODE_SINGLE_GAME || mode == MODE_PRIVATE_GAME) {
            fragmentPagerAdapter = new QuizFragmentPagerAdapter(getSupportFragmentManager(), quiz);
        }
        fragmentPagerAdapter.setAnswerListener(getAnswerListener());

        viewPager.setPagingEnabled(false);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(fragmentPagerAdapter);
        printCurrentRound(1);
        startTimer();
    }

    @SuppressLint("SetTextI18n")
    private void printCurrentRound(int currentQuestion) {
        if (mode == MODE_SINGLE_GAME || mode == MODE_ONLINE_GAME) {
            textViewRoundNumber.setText("Вопрос " + currentQuestion + ", Раунд " + getCurrentRound());
        } else {
            textViewRoundNumber.setText("Вопрос " + currentQuestion);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private int getCurrentRound() {
        int currentRound = 0;
        if (mode == MODE_SINGLE_GAME) {
            DBManager dbManager = new DBManager(this);
            currentRound = dbManager.getCompletedRoundsCount() + 1;
            dbManager.close();
            return currentRound;
        }

        if (mode == MODE_ONLINE_GAME) {
            if (room.getRounds() != null) {
                currentRound = room.getRounds().size();
            }
            return currentRound;
        }
        return currentRound;
    }

    private void startTimer() {
        progressBarTimer.setProgress(1500);
        countDownTimer = new CountDownTimer(TIME_FOR_ANSWER, TIME_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                int value = (int) millisUntilFinished / TIME_INTERVAL;
                value--;
                timeLeft = value;
                progressBarTimer.setProgress(value * 100);

            }

            public void onFinish() {
                timeLeft = 0;
                progressBarTimer.setProgress(0);
                answers.add(0);
                if (mode == MODE_ONLINE_GAME) {
                    Api.getSource().
                            answerOnQuestionInRoom(
                                    new AnswerOnQuestionInRoomRequestData
                                            (viewPager.getCurrentItem(), "0",
                                                    room.getRounds().size() - 1, room.getUuid(), 0))
                            .enqueue(ApiResponse.getCallback(getAskNoneResponseListener(0),
                                    QuizActivity.this));
                    fragmentPagerAdapter.disableInterface(viewPager.getCurrentItem());
                } else {
                    swipeQuestion();
                }
            }

        }.start();
    }

    private ApiResponse.ApiResponseListener<ResponseModel<Boolean>> getAskNoneResponseListener(int round) {
        return new ApiResponse.ApiResponseListener<ResponseModel<Boolean>>() {
            @Override
            public void onResponse(ResponseModel<Boolean> result) {
                swipeQuestion();
            }
        };
    }

    @Override
    protected void onDestroy() {
        App.Log("onDestroy");
        super.onDestroy();
        if (mode == MODE_ONLINE_GAME) {
            App.Log("online");
            if (!isOnlineQuizFinished) {
                App.Log("starting service...");
                Bundle extras = new Bundle();
                extras.putString(GiveUpService.ARG_ROOM_UUID, room.getUuid());
                Intent intent = new Intent(this, GiveUpService.class);
                intent.setAction(GiveUpService.ACTION_GIVE_UP);
                intent.putExtras(extras);
                startService(intent);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (mode == MODE_ONLINE_GAME) {
            MessageReporter.showConfirmForExitFromOnlineGame(this, getExitFromOnlineClickListener());
        } else if (mode == MODE_PRIVATE_GAME || mode == MODE_SINGLE_GAME) {
            MessageReporter.showConfirmForExitFromSingleGame(this, getExitClickListener());
        }

    }

    private DialogInterface.OnClickListener getExitFromOnlineClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exitFromGameLoading.show();
                countDownTimer.cancel();
                Api.getSource().giveUp(new GiveUpRequestData(room.getUuid()))
                        .enqueue(ApiResponse.getCallback(getGiveUpResponseListener(), getGiveUpFailureListener()));
            }
        };
    }

    private ApiResponse.ApiFailureListener getGiveUpFailureListener() {
        return new ApiResponse.ApiFailureListener() {
            @Override
            public void onFailure(int code, String message) {
                exitFromGameLoading.cancel();
                finish();
            }
        };
    }

    private ApiResponse.ApiResponseListener<ResponseModel<String>> getGiveUpResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<String>>() {
            @Override
            public void onResponse(ResponseModel<String> result) {
                exitFromGameLoading.cancel();
                if (result == null || result.getResult() == null) {
                    finish();
                }
                isOnlineQuizFinished = true;
                finish();
            }
        };
    }


    private DialogInterface.OnClickListener getExitClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                countDownTimer.cancel();
                finish();
            }
        };
    }

    private ArrayList<Integer> generateComputerAnswers() {
        ArrayList<Integer> computerAnswers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            int value = random.nextInt(2);
            computerAnswers.add(value);
        }
        return computerAnswers;
    }

    @OnClick(R.id.imageButtonBack)
    void onBackButtonClick() {
        onBackPressed();
    }


    private AnswerListener getAnswerListener() {
        return new AnswerListener() {
            @Override
            public void onQuestionAnswered(boolean isAnswerRight, int round) {
                if (isAnswerRight) {
                    answers.add(1);
                } else {
                    answers.add(0);
                }
                if (countDownTimer != null) {
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

    private void swipeQuestion() {
        int currentPosition = viewPager.getCurrentItem();
        if (currentPosition == 0) {
            viewPager.setCurrentItem(1, true);
            printCurrentRound(2);
            startTimer();
        }
        if (currentPosition == 1) {
            viewPager.setCurrentItem(2, true);
            printCurrentRound(3);
            startTimer();
        }
        if (currentPosition == 2) {
            switch (mode) {
                case MODE_SINGLE_GAME:
                    finish();
                    ArrayList<Integer> enemyAnswers = generateComputerAnswers();
                    startActivity(ResultsActivity.getInstanceForSingleGame(QuizActivity.this, answers, enemyAnswers));
                    break;

                case MODE_PRIVATE_GAME:
                    privateGame.addResultsToCurrentPlayer(answers);
                    privateGame.nextPlayer();
                    privateGame.nextTeam();
                    finish();
                    startActivity(PrivateGameResultsActivity.getInstanceForShowResults(QuizActivity.this, privateGame));
                    break;

                case MODE_ONLINE_GAME:
                    finish();
                    isOnlineQuizFinished = true;
                    startActivity(ResultsActivity.getInstanceForOnlineGame(QuizActivity.this, room));
                    break;
            }

        }
    }

    @Override
    public void onFailure(int code, String message) {
        MessageReporter.showMessage(this,
                "Ошибка", message, getFailureExitListener(), false);
    }


    public DialogInterface.OnClickListener getFailureExitListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
    }
}
