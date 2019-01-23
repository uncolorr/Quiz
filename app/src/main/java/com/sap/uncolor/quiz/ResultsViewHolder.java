package com.sap.uncolor.quiz;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.results_activity.ResultActivityPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsViewHolder extends RecyclerView.ViewHolder implements ApiResponse.ApiFailureListener {

    @BindView(R.id.viewMyRound1)
    View viewMyRound1;

    @BindView(R.id.viewMyRound2)
    View viewMyRound2;

    @BindView(R.id.viewMyRound3)
    View viewMyRound3;

    @BindView(R.id.viewEnemyRound1)
    View viewEnemyRound1;

    @BindView(R.id.viewEnemyRound2)
    View viewEnemyRound2;

    @BindView(R.id.viewEnemyRound3)
    View viewEnemyRound3;

    @BindView(R.id.buttonPlay)
    Button buttonPlay;

    @BindView(R.id.textViewRoundNumber)
    TextView textViewRoundNumber;

    private ResultActivityPresenter presenter;

    public ResultsViewHolder(@NonNull View itemView, ResultActivityPresenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.presenter = presenter;
    }

    public void bind(Results results){
        if(results.getState() == Results.STATE_NEXT_GAME){
            viewMyRound1.setVisibility(View.INVISIBLE);
            viewMyRound2.setVisibility(View.INVISIBLE);
            viewMyRound3.setVisibility(View.INVISIBLE);
            viewEnemyRound1.setVisibility(View.INVISIBLE);
            viewEnemyRound2.setVisibility(View.INVISIBLE);
            viewEnemyRound3.setVisibility(View.INVISIBLE);
            buttonPlay.setVisibility(View.VISIBLE);
        }
        else if (results.getState() == Results.STATE_COMPLETED) {
            drawMyResults(results.getMyAnswers());
            drawEnemyResults(results.getEnemyAnswers());
        }
        textViewRoundNumber.setText((getAdapterPosition() + 1) + " Раунд");
    }

    @OnClick(R.id.buttonPlay)
    void onButtonPlayClick(){
        presenter.onStartGame();
    }

    private void drawMyResults(List<Integer> answers){
        if(answers.get(0) > 0){
            viewMyRound1.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.result_right));
        }
        else {
            viewMyRound1.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.result_wrong));
        }

        if(answers.get(1) > 0){
            viewMyRound2.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.result_right));
        }
        else {
            viewMyRound2.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.result_wrong));
        }

        if(answers.get(2) > 0){
            viewMyRound3.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.result_right));
        }
        else {
            viewMyRound3.setBackground(ContextCompat.getDrawable(itemView.getContext(), R.drawable.result_wrong));
        }
    }

    private void drawEnemyResults(List<Integer> computerAnswers){
        if(computerAnswers.get(0) > 0){
            viewEnemyRound1.setBackground(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.result_right));
        }
        else {
            viewEnemyRound1.setBackground(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.result_wrong));
        }

        if(computerAnswers.get(1) > 0){
            viewEnemyRound2.setBackground(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.result_right));
        }
        else {
            viewEnemyRound2.setBackground(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.result_wrong));
        }

        if(computerAnswers.get(2) > 0){
            viewEnemyRound3.setBackground(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.result_right));
        }
        else {
            viewEnemyRound3.setBackground(ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.result_wrong));
        }
    }

    @Override
    public void onFailure(int code, String message) {

    }
}
