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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizFragment extends Fragment {

    private static final String ARG_MODEL = "quiz";
    private static final String ARG_ROUND = "round";


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

    public static QuizFragment newInstance(Quiz model, int round) {

        Bundle args = new Bundle();
        QuizFragment fragment = new QuizFragment();
        args.putSerializable(ARG_MODEL, model);
        args.putInt(ARG_ROUND, round);
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
            quiz = (Quiz) getArguments().getSerializable(ARG_MODEL);
            round = getArguments().getInt(ARG_ROUND);
        }
        if(quiz != null){
            textViewQuestion.setText(quiz.getQuestion(round).getQuestion());
            textViewVariant1.setText(quiz.getQuestion(round).getVariant1());
            textViewVariant2.setText(quiz.getQuestion(round).getVariant2());
            textViewVariant3.setText(quiz.getQuestion(round).getVariant3());
            textViewVariant4.setText(quiz.getQuestion(round).getVariant4());
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void answer(int variant){
        if(answerListener != null){
            checkAnswer(variant, round);
            answerListener.onQuestionAnswered(variant, round);
        }
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

    private void checkAnswer(int variant, int round){
        if(getContext() != null) {
            switch (variant) {
                case Quiz.VARIANT_ONE:
                    if (variant == quiz.getQuestion(round).getVariantRight()) {
                        textViewVariant1.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_right));
                        textViewVariant1.setTextColor(Color.WHITE);
                    } else {
                        textViewVariant1.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_wrong));
                        textViewVariant1.setTextColor(Color.WHITE);
                    }
                    break;

                case Quiz.VARIANT_TWO:
                    if (variant == quiz.getQuestion(round).getVariantRight()) {
                        textViewVariant2.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_right));
                        textViewVariant2.setTextColor(Color.WHITE);
                    } else {
                        textViewVariant2.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_wrong));
                        textViewVariant2.setTextColor(Color.WHITE);
                    }
                    break;

                case Quiz.VARIANT_THREE:
                    if (variant == quiz.getQuestion(round).getVariantRight()) {
                        textViewVariant3.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_right));
                        textViewVariant3.setTextColor(Color.WHITE);
                    } else {
                        textViewVariant3.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_wrong));
                        textViewVariant3.setTextColor(Color.WHITE);
                    }
                    break;

                case Quiz.VARIANT_FOUR:
                    if (variant == quiz.getQuestion(round).getVariantRight()) {
                        textViewVariant4.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_right));
                        textViewVariant4.setTextColor(Color.WHITE);
                    } else {
                        textViewVariant4.setBackground(ContextCompat.getDrawable(getContext(),
                                R.drawable.button_answer_wrong));
                        textViewVariant4.setTextColor(Color.WHITE);
                    }
                    break;
            }
        }
    }
}
