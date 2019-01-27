package com.sap.uncolor.quiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sap.uncolor.quiz.models.PrivateGamePlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewPlayerName)
    TextView textViewPlayerName;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(PrivateGamePlayer privateGamePlayer){
        textViewPlayerName.setText(privateGamePlayer.getName());
    }
}
