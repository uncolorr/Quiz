package com.sap.uncolor.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivateGameResultsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewResults)
    RecyclerView recyclerViewResults;

    @BindView(R.id.textViewNextPlayer)
    TextView textViewNextPlayer;

    private UniversalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_game_results);
        ButterKnife.bind(this);
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new TeamResultsViewRenderer(Team.TYPE, this));
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        textViewNextPlayer.setText("Отвечает Антон, Команда А");
        DBManager dbManager = new DBManager(this);
        ArrayList<Team> teams = dbManager.getPrivateGameTeamsFromDatabase();
        for (int i = 0; i < teams.size(); i++) {
            adapter.add(teams.get(i));
        }
    }

    @OnClick(R.id.linearLayoutStartGame)
    void onStartGameButtonClick(){

    }


}
