package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sap.uncolor.quiz.models.Team;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

class TeamViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewPlayersCount)
    TextView textViewPlayersCount;

    @BindView(R.id.textViewTeamName)
    TextView textViewTeamName;

    public TeamViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Team team){
        textViewTeamName.setText(team.getName());
        textViewPlayersCount.setText("Участников: "+ team.getPrivateGamePlayers().size());
    }

    @OnClick(R.id.imageButtonRemove)
    void onButtonRemoveClick(){

    }
}
