package com.sap.uncolor.quiz.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sap.uncolor.quiz.ItemModel;
import com.sap.uncolor.quiz.PlayerViewRenderer;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.create_private_table_activity.CreatePrivateTableActivityPresenter;
import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.CreateTeamListener;

import java.util.ArrayList;

public class AddPlayersDialog {

    private AlertDialog.Builder builder;

    private EditText editTextPlayerName;
    private RecyclerView recyclerViewPlayers;
    private ImageButton imageButtonAddPlayer;

    private CreateTeamListener createTeamListener;

    private UniversalAdapter adapter;

    private Context context;

    private CreatePrivateTableActivityPresenter presenter;

    public AlertDialog create(final Context context, CreatePrivateTableActivityPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
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
                    ArrayList<PrivateGamePlayer> players = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        players.add((PrivateGamePlayer) items.get(i));
                    }
                    if(players.size() == 0){
                        Toast.makeText(context, "Добавьте хотя бы 1 игрока в команду", Toast.LENGTH_LONG).show();
                        return;
                    }
                    createTeamListener.onCreateTeam(players);
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
        adapter.registerRenderer(new PlayerViewRenderer(PrivateGamePlayer.TYPE, context, presenter));
        recyclerViewPlayers.setAdapter(adapter);
        recyclerViewPlayers.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
    }

    public void setOnCreateTeamListener(CreateTeamListener createTeamListener){
        this.createTeamListener = createTeamListener;
    }

    public void removePlayer(int index){
        adapter.remove(index);
    }

}


