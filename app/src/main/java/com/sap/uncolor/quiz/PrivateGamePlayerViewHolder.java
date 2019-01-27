package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sap.uncolor.quiz.models.PrivateGamePlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivateGamePlayerViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.textViewPlayerName)
    TextView textViewPlayerName;

    @BindView(R.id.textViewPointsRight)
    TextView textViewPointsRight;

    @BindView(R.id.textViewPointsWrong)
    TextView textViewPointsWrong;

    public PrivateGamePlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void bind(PrivateGamePlayer privateGamePlayer){
        textViewPlayerName.setText(privateGamePlayer.getName());
        textViewPointsRight.setText(Integer.toString(privateGamePlayer.getPointsRight()));
        textViewPointsWrong.setText(Integer.toString(privateGamePlayer.getPointsWrong()));
    }
}
