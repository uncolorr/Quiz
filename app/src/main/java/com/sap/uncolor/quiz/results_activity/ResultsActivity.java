package com.sap.uncolor.quiz.results_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.ResultsViewRenderer;
import com.sap.uncolor.quiz.RoundViewRenderer;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.Round;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.universal_adapter.ResultsAdapter;
import com.sap.uncolor.quiz.utils.MessageReporter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsActivity extends AppCompatActivity implements ResultActivityContract.View {

    private static final String ARG_GAME_TYPE = "game_type";
    private static final String ARG_ANSWERS = "answers";
    private static final String ARG_ENEMY_ANSWERS = "enemy_answers";
    private static final String ARG_ROOM = "room";

    private static final int GAME_TYPE_SINGLE = 1;
    private static final int GAME_TYPE_ONLINE = 2;

    @BindView(R.id.recyclerViewResults)
    RecyclerView recyclerViewResults;

    @BindView(R.id.textViewResultScores)
    TextView textViewResultScores;

    @BindView(R.id.textViewMyUsername)
    TextView textViewMyUsername;

    @BindView(R.id.textViewEnemyUsername)
    TextView textViewEnemyUsername;

    private ResultsAdapter adapter;

    private DBManager dbManager;

    private ResultActivityPresenter presenter;

    private AlertDialog loadingDialog;

    private Room room;

    private Handler handler;

    private Runnable updateOnlineGameInfoRunnable;

    private int gameType;


    public static Intent getInstanceForSingleGame(Context context,
                                                  ArrayList<Integer> answers,
                                                  ArrayList<Integer> enemyAnswers) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putIntegerArrayListExtra(ARG_ANSWERS, answers);
        intent.putIntegerArrayListExtra(ARG_ENEMY_ANSWERS, enemyAnswers);
        intent.putExtra(ARG_GAME_TYPE, GAME_TYPE_SINGLE);
        return intent;
    }

    public static Intent getInstanceForOnlineGame(Context context, Room room) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(ARG_GAME_TYPE, GAME_TYPE_ONLINE);
        intent.putExtra(ARG_ROOM, room);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        presenter = new ResultActivityPresenter(this, this);
        dbManager = new DBManager(this);
        initResultsTable();
        gameType = getIntent().getIntExtra(ARG_GAME_TYPE, 0);
        if (gameType == GAME_TYPE_SINGLE) {
            ArrayList<Integer> answers = getIntent().getIntegerArrayListExtra(ARG_ANSWERS);
            ArrayList<Integer> enemyAnswers = getIntent().getIntegerArrayListExtra(ARG_ENEMY_ANSWERS);
            dbManager.addSingleGameResultsToDatabase(answers, enemyAnswers);
            ArrayList<Results> results = dbManager.getSingleGameResultsFromDatabase();
            for (int i = 0; i < results.size(); i++) {
                adapter.add(results.get(i));
            }
            if (dbManager.getCompletedRoundsCount() < 5) {
                adapter.add(new Results(Results.STATE_NEXT_GAME));
            }
            textViewResultScores.setText(adapter.getMyResultsCounter() + " - " + adapter.getEnemyResultsCounter());
        }

        if (gameType == GAME_TYPE_ONLINE) {
            App.Log("game type: online" );
            room = (Room) getIntent().getSerializableExtra(ARG_ROOM);
            if (room.getRounds() != null) {
                for (int i = 0; i < room.getRounds().size(); i++) {
                    if (room.getRounds().get(i).getCreatorLastQuestion() != 3) {
                        room.getRounds().get(i).setState(Round.STATE_NEXT_GAME);
                        adapter.add(room.getRounds().get(i));
                    } else {
                        room.getRounds().get(i).setState(Round.STATE_COMPLETED);
                        adapter.add(room.getRounds().get(i));
                    }

                    if(room.isMine()){
                        room.getRounds().get(i).setMine(true);
                    }else {
                        room.getRounds().get(i).setMine(false);
                    }
                }
            }

            if (room.getCompetitor() == null) {
                textViewMyUsername.setText(room.getCreator().getLogin());
                textViewEnemyUsername.setText("N/A");
                return;
            }

            if(room.isMine()) {
                App.Log("mine");
                textViewMyUsername.setText(room.getCompetitor().getLogin());
                textViewEnemyUsername.setText(room.getCreator().getLogin());
            } else  {
                App.Log("not mine");
                textViewMyUsername.setText(room.getCreator().getLogin());
                textViewEnemyUsername.setText(room.getCompetitor().getLogin());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gameType == GAME_TYPE_ONLINE) {
            handler = new Handler();
            updateOnlineGameInfoRunnable = new Runnable() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void run() {
                    presenter.onUpdateInfoAboutOnlineGame(room);
                    handler.postDelayed(updateOnlineGameInfoRunnable, 5000);
                }
            };
            handler.post(updateOnlineGameInfoRunnable);
        }
    }


    @OnClick(R.id.buttonBack)
    void onBackButtonClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switch (gameType) {
            case GAME_TYPE_SINGLE:
                if (dbManager.getCompletedRoundsCount() >= 5) {
                    dbManager.clearSingleGameResults();
                }
                dbManager.close();
                break;
            case GAME_TYPE_ONLINE:
                handler.removeCallbacks(updateOnlineGameInfoRunnable);
                break;
        }

    }

    private void initResultsTable() {
        adapter = new ResultsAdapter();
        adapter.registerRenderer(new ResultsViewRenderer(Results.TYPE, this, presenter));
        adapter.registerRenderer(new RoundViewRenderer(Round.TYPE, this, presenter));
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showProcessingDialog() {
        loadingDialog.show();
    }

    @Override
    public void hideProcessingDialog() {
        loadingDialog.cancel();
    }

    @Override
    public void startSingleGame(Quiz quiz) {
        startActivity(QuizActivity.getInstanceForSingleGame(this, quiz));
        finish();
    }

    @Override
    public void startOnlineGame() {
        if (room == null) {
            return;
        }
        startActivity(QuizActivity.getInstanceForOnlineGame(this, room));
        finish();
    }

    @Override
    public void showErrorMessage() {
        MessageReporter.showMessage(this,
                "Ошибка",
                "Ошибка создания игры");
    }

    @Override
    public void updateInfoAboutOnlineGame(Room room) {

        App.Log("update room info");
        textViewMyUsername.setText(room.getCreator().getLogin());
        if (room.getCompetitor() == null) {
            textViewEnemyUsername.setText("N/A");
        } else {
            textViewEnemyUsername.setText(room.getCompetitor().getLogin());
        }
        if (room.getRounds() == null) {
            return;
        }

        adapter.clear();
        for (int i = 0; i < room.getRounds().size(); i++) {
            if (room.getRounds().get(i).getCreatorLastQuestion() != 3) {
                room.getRounds().get(i).setState(Round.STATE_NEXT_GAME);
                adapter.add(room.getRounds().get(i));
            } else {
                room.getRounds().get(i).setState(Round.STATE_COMPLETED);
                adapter.add(room.getRounds().get(i));
            }
            if(room.isMine()){
                room.getRounds().get(i).setMine(true);
            }else {
                room.getRounds().get(i).setMine(false);
            }
        }
    }
}
