package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamResultsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewTeamName)
    TextView textViewTeamName;

    @BindView(R.id.textViewTeamPoints)
    TextView textViewTeamPoints;

    @BindView(R.id.recyclerViewPlayers)
    RecyclerView recyclerViewPlayers;

    private UniversalAdapter adapter;

    public TeamResultsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new PrivateGamePlayerViewRenderer(PrivateGamePlayer.TYPE, itemView.getContext()));
        recyclerViewPlayers.setAdapter(adapter);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @SuppressLint("SetTextI18n")
    public void bind(Team team){
        textViewTeamName.setText(team.getName());
        textViewTeamPoints.setText(Integer.toString(team.getTeamPoints()));
        for (int i = 0; i < team.getPrivateGamePlayers().size(); i++) {
            adapter.add(team.getPrivateGamePlayers().get(i));
        }
    }
}
