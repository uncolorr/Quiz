package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.sap.uncolor.quiz.models.Player;
import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.AddPlayersDialog;
import com.sap.uncolor.quiz.utils.CreateTeamListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePrivateTableActivity extends AppCompatActivity implements CreateTeamListener{

    @BindView(R.id.editTextTeamName)
    EditText editTextTeamName;

    @BindView(R.id.recyclerViewTeams)
    RecyclerView recyclerViewTeams;

    private AddPlayersDialog addPlayersDialog;

    private UniversalAdapter adapter;

    public static Intent getInstance(Context context){
        return new Intent(context, CreatePrivateTableActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_private_tabel);
        ButterKnife.bind(this);
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new TeamViewRenderer(Team.TYPE, this));
    }

    @OnClick(R.id.buttonCreatePrivateTable)
    void onButtonCreateTableClick(){

    }

    @OnClick(R.id.imageButtonAddTeam)
    void onButtonAddTeamClick(){
        addPlayersDialog = new AddPlayersDialog();
        addPlayersDialog.setOnCreateTeamListener(this);
        AlertDialog dialog = addPlayersDialog.create(this);
        dialog.show();
    }


    @Override
    public void onCreateTeam(ArrayList<Player> players) {
        Team team = new Team(editTextTeamName.getText().toString(), players);
        adapter.add(team);
    }
}
