package com.sap.uncolor.quiz;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sap.uncolor.quiz.models.Round;
import com.sap.uncolor.quiz.results_activity.ResultActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RoundViewHolder extends RecyclerView.ViewHolder{

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

    private Round round;

    private ResultActivityPresenter presenter;

    public RoundViewHolder(@NonNull View itemView, ResultActivityPresenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.presenter = presenter;
    }

    public void bind(Round round){
        this.round = round;
        if(round.getState() == Round.STATE_NEXT_GAME){
            viewMyRound1.setVisibility(View.INVISIBLE);
            viewMyRound2.setVisibility(View.INVISIBLE);
            viewMyRound3.setVisibility(View.INVISIBLE);
            buttonPlay.setVisibility(View.VISIBLE);
        }
        else if (round.getState() == Round.STATE_COMPLETED) {
            buttonPlay.setVisibility(View.INVISIBLE);
        }
        drawMyResults();
        drawEnemyResults();
        textViewRoundNumber.setText((getAdapterPosition() + 1) + " Раунд");
    }

    private void drawEnemyResults() {
        if(round.getCompetitorLastQuestion() == 0){
            viewEnemyRound1.setBackground(ContextCompat
                    .getDrawable(itemView.getContext(), R.drawable.result_none));
            viewEnemyRound2.setBackground(ContextCompat
                    .getDrawable(itemView.getContext(), R.drawable.result_none));
            viewEnemyRound3.setBackground(ContextCompat
                    .getDrawable(itemView.getContext(), R.drawable.result_none));
            return;
        }

        String competitorMask = String.format("%3s", Integer.toBinaryString(round.getCompetitorMask()))
                .replace(' ', '0');
        byte[] competitorMaskBytes = competitorMask.getBytes();
        for (int i = 0; i < competitorMaskBytes.length ; i++) {
            if(i == 0){
                if(competitorMaskBytes[i] == '1'){
                    viewEnemyRound1.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_right));
                }
                else if(competitorMaskBytes[i] == '0'){
                    viewEnemyRound1.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_wrong));
                }
            }

            if(i == 1){
                if(competitorMaskBytes[i] == '1'){
                    viewEnemyRound2.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_right));
                }
                else if(competitorMaskBytes[i] == '0'){
                    viewEnemyRound2.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_wrong));
                }
            }

            if(i == 2){
                if(competitorMaskBytes[i] == '1'){
                    viewEnemyRound3.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_right));
                }
                else if(competitorMaskBytes[i] == '0'){
                    viewEnemyRound3.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_wrong));
                }
            }

        }
    }

    private void drawMyResults() {
        String creatorMask = String.format("%3s", Integer.toBinaryString(round.getCreatorMask()))
                .replace(' ', '0');
        byte[] creatorMaskAsBytes = creatorMask.getBytes();
        for (int i = 0; i < creatorMaskAsBytes.length ; i++) {
            if(i == 0){
                if(creatorMaskAsBytes[i] == '1'){
                    viewMyRound1.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_right));
                }
                else if(creatorMaskAsBytes[i] == '0'){
                    viewMyRound1.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_wrong));
                }
            }

            if(i == 1){
                if(creatorMaskAsBytes[i] == '1'){
                    viewMyRound2.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_right));
                }
                else if(creatorMaskAsBytes[i] == '0'){
                    viewMyRound2.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_wrong));
                }
            }

            if(i == 2){
                if(creatorMaskAsBytes[i] == '1'){
                    viewMyRound3.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_right));
                }
                else if(creatorMaskAsBytes[i] == '0'){
                    viewMyRound3.setBackground(ContextCompat
                            .getDrawable(itemView.getContext(), R.drawable.result_wrong));
                }
            }

        }
    }

    @OnClick(R.id.buttonPlay)
    void onButtonPlayClick(){
        presenter.onStartOnlineGame();
    }
}
