package com.sap.uncolor.quiz;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sap.uncolor.quiz.models.Results;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultsViewHolder extends RecyclerView.ViewHolder {

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

    public ResultsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Results results){
        ArrayList<Integer> answers = results.getAnswers();
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
}
