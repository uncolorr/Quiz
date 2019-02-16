package com.sap.uncolor.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.request_datas.AnswerOnQuestionInRoomRequestData;
import com.sap.uncolor.quiz.models.request_datas.CheckAnswerForOfflineRequestData;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.utils.MessageReporter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizFragment extends Fragment implements ApiResponse.ApiFailureListener{

    private static final String ARG_QUIZ = "quiz";
    private static final String ARG_ROUND = "round";
    private static final String ARG_MODE = "mode";
    private static final String ARG_ROOM = "room";
    private static final String ARG_QUESTION_INDEX = "question_index";

    private static final int MODE_ONLINE = 1;
    private static final int MODE_OFFLINE = 2;


    @BindView(R.id.textViewQuestion)
    TextView textViewQuestion;

    @BindView(R.id.textViewVariant1)
    TextView textViewVariant1;

    @BindView(R.id.textViewVariant2)
    TextView textViewVariant2;

    @BindView(R.id.textViewVariant3)
    TextView textViewVariant3;

    @BindView(R.id.textViewVariant4)
    TextView textViewVariant4;

    private AnswerListener answerListener;

    private Quiz quiz;
    private int round;
    private int mode;
    private Room room;
    private int question_index;

    public static QuizFragment newInstanceForOffline(Quiz quiz, int round) {
        Bundle args = new Bundle();
        QuizFragment fragment = new QuizFragment();
        args.putSerializable(ARG_QUIZ, quiz);
        args.putInt(ARG_ROUND, round);
        args.putInt(ARG_MODE, MODE_OFFLINE);
        fragment.setArguments(args);
        return fragment;
    }

    public static QuizFragment newInstanceForOnline(Quiz quiz, int round, Room room, int question_index) {
        Bundle args = new Bundle();
        QuizFragment fragment = new QuizFragment();
        args.putSerializable(ARG_QUIZ, quiz);
        args.putInt(ARG_ROUND, round);
        args.putSerializable(ARG_ROOM, room);
        args.putInt(ARG_MODE, MODE_ONLINE);
        args.putInt(ARG_QUESTION_INDEX, question_index);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        ButterKnife.bind(this, view);
        if(getArguments() != null) {
            quiz = (Quiz) getArguments().getSerializable(ARG_QUIZ);
            room = (Room) getArguments().getSerializable(ARG_ROOM);
            round = getArguments().getInt(ARG_ROUND);
            mode = getArguments().getInt(ARG_MODE);
            question_index = getArguments().getInt(ARG_QUESTION_INDEX);
        }
        if(quiz != null){
            Question question = null;
            if(mode == MODE_ONLINE){
                question = quiz.getQuestionForOnline(question_index);
            }
            else if(mode == MODE_OFFLINE){
                question = quiz.getQuestion(round);
            }
            assert question != null;
            textViewQuestion.setText(question.getQuestion());
            textViewVariant1.setText(question.getVariant1());
            textViewVariant2.setText(question.getVariant2());
            textViewVariant3.setText(question.getVariant3());
            textViewVariant4.setText(question.getVariant4());
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void answer(int variant){
        if(answerListener != null){
            switch (mode){
                case MODE_ONLINE:
                    checkAnswerForOnlineGames(variant, question_index, round);
                    break;
                case MODE_OFFLINE:
                    checkAnswerForOfflineGames(variant, round);
                    break;
            }
            disableInterface();

        }
    }

    private void checkAnswerForOnlineGames(int variant, int question_index, int round) {
        if(getContext() != null) {
            int points = QuizActivity.getPoints();
            switch (variant) {
                case Quiz.VARIANT_ONE:
                    Api.getSource().
                            answerOnQuestionInRoom(
                                    new AnswerOnQuestionInRoomRequestData
                                            (question_index, "1", round, room.getUuid(), points))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant1.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant1.setTextColor(Color.WHITE);
                    break;

                case Quiz.VARIANT_TWO:
                    Api.getSource().
                            answerOnQuestionInRoom(
                                    new AnswerOnQuestionInRoomRequestData
                                            (question_index, "2", round, room.getUuid(), points))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant2.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant2.setTextColor(Color.WHITE);
                    break;


                case Quiz.VARIANT_THREE:
                    Api.getSource().answerOnQuestionInRoom(
                                    new AnswerOnQuestionInRoomRequestData
                                            (question_index, "3", round, room.getUuid(), points))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant3.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant3.setTextColor(Color.WHITE);
                    break;

                case Quiz.VARIANT_FOUR:
                    Api.getSource().answerOnQuestionInRoom(
                                    new AnswerOnQuestionInRoomRequestData
                                            (question_index, "4", round, room.getUuid(), points))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant4.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant4.setTextColor(Color.WHITE);
                    break;
            }
        }
    }

    public void disableInterface(){
        textViewVariant1.setEnabled(false);
        textViewVariant2.setEnabled(false);
        textViewVariant3.setEnabled(false);
        textViewVariant4.setEnabled(false);
    }

    @OnClick(R.id.textViewVariant1)
    void onVariant1Click(){
        answer(Quiz.VARIANT_ONE);
    }

    @OnClick(R.id.textViewVariant2)
    void onVariant2Click(){
        answer(Quiz.VARIANT_TWO);
    }

    @OnClick(R.id.textViewVariant3)
    void onVariant3Click(){
        answer(Quiz.VARIANT_THREE);
    }

    @OnClick(R.id.textViewVariant4)
    void onVariant4Click(){
        answer(Quiz.VARIANT_FOUR);
    }

    public void setAnswerListener(AnswerListener listener){
        this.answerListener = listener;
    }

    private boolean checkAnswerForOfflineGames(int variant, int round){
        if(getContext() != null) {
            switch (variant) {
                case Quiz.VARIANT_ONE:
                    Api.getSource().
                            checkAnswer(new CheckAnswerForOfflineRequestData(quiz.getQuestion(round).getId(), "1"))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant1.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant1.setTextColor(Color.WHITE);
                    break;

                case Quiz.VARIANT_TWO:
                    Api.getSource().
                            checkAnswer(new CheckAnswerForOfflineRequestData(quiz.getQuestion(round).getId(), "2"))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant2.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant2.setTextColor(Color.WHITE);
                    break;


                case Quiz.VARIANT_THREE:
                    Api.getSource().
                            checkAnswer(new CheckAnswerForOfflineRequestData(quiz.getQuestion(round).getId(), "3"))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant3.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant3.setTextColor(Color.WHITE);
                    break;

                case Quiz.VARIANT_FOUR:
                    Api.getSource().
                            checkAnswer(new CheckAnswerForOfflineRequestData(quiz.getQuestion(round).getId(), "4"))
                            .enqueue(ApiResponse.getCallback(getApiResponseListener(variant), this));
                    textViewVariant4.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.button_answer_current));
                    textViewVariant4.setTextColor(Color.WHITE);
                    break;
            }
        }
        return false;
    }

    private ApiResponse.ApiResponseListener<ResponseModel<Boolean>> getApiResponseListener(final int variant) {
        return new ApiResponse.ApiResponseListener<ResponseModel<Boolean>>() {
            @Override
            public void onResponse(ResponseModel<Boolean> result) {
                if (getContext() != null) {
                    if (result == null) {
                        MessageReporter.showMessage(getContext(),
                                "Ошибка",
                                "Ошибка создания игры");
                        return;
                    }
                    if (result.getResult() == null) {
                        MessageReporter.showMessage(getContext(),
                                "Ошибка",
                                "Ошибка при проверке результата");
                        return;
                    } else {
                        switch (variant) {
                            case Quiz.VARIANT_ONE:
                                if (result.getResult()) {
                                    textViewVariant1.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_right));
                                    textViewVariant1.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(true, round);

                                } else {
                                    textViewVariant1.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_wrong));
                                    textViewVariant1.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(false, round);
                                }
                                break;


                            case Quiz.VARIANT_TWO:
                                if (result.getResult()) {
                                    textViewVariant2.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_right));
                                    textViewVariant2.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(true, round);

                                } else {
                                    textViewVariant2.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_wrong));
                                    textViewVariant2.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(false, round);
                                }
                                break;


                            case Quiz.VARIANT_THREE:
                                if (result.getResult()) {
                                    textViewVariant3.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_right));
                                    textViewVariant3.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(true, round);

                                } else {
                                    textViewVariant3.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_wrong));
                                    textViewVariant3.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(false, round);
                                }
                                break;

                            case Quiz.VARIANT_FOUR:
                                if (result.getResult()) {
                                    textViewVariant4.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_right));
                                    textViewVariant4.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(true, round);

                                } else {
                                    textViewVariant4.setBackground(ContextCompat.getDrawable(getContext(),
                                            R.drawable.button_answer_wrong));
                                    textViewVariant4.setTextColor(Color.WHITE);
                                    answerListener.onQuestionAnswered(false, round);
                                }
                                break;
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onFailure(int code, String message) {
        MessageReporter.showMessage(getContext(),
                "Ошибка",
                "Неизвестная ошибка");
    }
}
