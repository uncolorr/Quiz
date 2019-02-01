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

import com.sap.uncolor.quiz.ItemModel;
import com.sap.uncolor.quiz.PlayerViewRenderer;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;

import java.util.ArrayList;

public class AddPlayersDialog {

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
        builder.setPositiveButton("СОЗДАТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(createTeamListener != null){
                    ArrayList<ItemModel> items = adapter.getItems();
                    ArrayList<PrivateGamePlayer> result = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        result.add((PrivateGamePlayer) items.get(i));
                    }
                    createTeamListener.onCreateTeam(result);
                }
            }
        });
        builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
                PrivateGamePlayer privateGamePlayer = new PrivateGamePlayer(editTextPlayerName.getText().toString());
                adapter.add(privateGamePlayer);
                editTextPlayerName.getText().clear();
            }
        };
    }

    private void initRecyclerView(){
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new PlayerViewRenderer(PrivateGamePlayer.TYPE, context));
        recyclerViewPlayers.setAdapter(adapter);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
    }

    public void setOnCreateTeamListener(CreateTeamListener createTeamListener){
        this.createTeamListener = createTeamListener;
    }

}

