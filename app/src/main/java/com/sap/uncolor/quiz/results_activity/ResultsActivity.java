package com.sap.uncolor.quiz.results_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.ResultsViewRenderer;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.universal_adapter.ResultsAdapter;
import com.sap.uncolor.quiz.utils.MessageReporter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsActivity extends AppCompatActivity implements ResultActivityContract.View{

    private static final String ARG_GAME_TYPE = "game_type";
    private static final String ARG_ANSWERS = "answers";
    private static final String ARG_ENEMY_ANSWERS = "enemy_answers";

    private static final int GAME_TYPE_SINGLE = 1;
    private static final int GAME_TYPE_ONLINE = 2;
    private static final int GAME_TYPE_PRIVATE = 3;

    @BindView(R.id.recyclerViewResults)
    RecyclerView recyclerViewResults;

    @BindView(R.id.textViewResultScores)
    TextView textViewResultScores;

    private ResultsAdapter adapter;

    private DBManager dbManager;

    private ResultActivityPresenter presenter;

    private AlertDialog loadingDialog;


    public static Intent getInstanceForSingleGame(Context context,
                                                  ArrayList<Integer> answers,
                                                  ArrayList<Integer> enemyAnswers){
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putIntegerArrayListExtra(ARG_ANSWERS,  answers);
        intent.putIntegerArrayListExtra(ARG_ENEMY_ANSWERS, enemyAnswers);
        intent.putExtra(ARG_GAME_TYPE, GAME_TYPE_SINGLE);
        return intent;
    }

    public static Intent getInstanceForOnlineGame(Context context){
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(ARG_GAME_TYPE, GAME_TYPE_ONLINE);
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
        int gameType = getIntent().getIntExtra(ARG_GAME_TYPE, 0);
        if(gameType == GAME_TYPE_SINGLE){
            ArrayList<Integer> answers = getIntent().getIntegerArrayListExtra(ARG_ANSWERS);
            ArrayList<Integer> enemyAnswers = getIntent().getIntegerArrayListExtra(ARG_ENEMY_ANSWERS);
            dbManager.addResultToDatabase(answers, enemyAnswers);
            ArrayList<Results> results = dbManager.getResultsFromDatabase();
            for (int i = 0; i < results.size(); i++) {
                adapter.add(results.get(i));
            }
            if(adapter.getItemCount() < 5){
                adapter.add(new Results(Results.STATE_NEXT_GAME));
            }
            textViewResultScores.setText(adapter.getMyResultsCounter() + " - " + adapter.getEnemyResultsCounter());
        }

        if(gameType == GAME_TYPE_ONLINE){

        }
    }

    @OnClick(R.id.buttonBack)
    void onBackButtonClick(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter.getItemCount() > 5){
            dbManager.clearDatabase();
        }
        dbManager.close();
    }

    private void initResultsTable(){
        adapter = new ResultsAdapter();
        adapter.registerRenderer(new ResultsViewRenderer(Results.TYPE, this, presenter));
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
    public void startGame(Quiz quiz) {
        startActivity(QuizActivity.getInstance(this, quiz));
    }

    @Override
    public void showErrorMessage() {
        MessageReporter.showMessage(this,
                "Ошибка",
                "Ошибка создания игры");
    }
}
