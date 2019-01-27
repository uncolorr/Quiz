package com.sap.uncolor.quiz.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.sap.uncolor.quiz.PlayerViewRenderer;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.models.Player;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;

import java.util.ArrayList;

public class AddPlayersDialog {

    private ArrayList<Player> players = new ArrayList<>();

    private AlertDialog.Builder builder;

    private EditText editTextPlayerName;
    private RecyclerView recyclerViewPlayers;
    private ImageButton imageButtonAddPlayer;

    private CreateTeamListener createTeamListener;

    private UniversalAdapter adapter;

    private Context context;

    public AlertDialog create(Context context) {
        this.context = context;
        builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_players, null);
        editTextPlayerName = view.findViewById(R.id.editTextPlayerName);
        imageButtonAddPlayer = view.findViewById(R.id.imageButtonAddPlayer);
        imageButtonAddPlayer.setOnClickListener(getOnAddButtonClickListener());
        recyclerViewPlayers = view.findViewById(R.id.recyclerViewPlayers);
        initRecyclerView();
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(createTeamListener != null){
                    createTeamListener.onCreateTeam(players);
                }
            }
        });
        return builder.create();
    }

    private View.OnClickListener getOnAddButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextPlayerName.getText().toString().isEmpty()) {
                    return;
                }
                Player player = new Player(editTextPlayerName.getText().toString());
                //players.add(player);
                adapter.add(player);

            }
        };
    }

    private void initRecyclerView(){
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new PlayerViewRenderer(Player.TYPE, context));
        recyclerViewPlayers.setAdapter(adapter);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
    }
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setOnCreateTeamListener(CreateTeamListener createTeamListener){
        this.createTeamListener = createTeamListener;
    }

}


