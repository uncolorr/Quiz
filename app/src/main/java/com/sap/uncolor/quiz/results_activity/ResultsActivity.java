package com.sap.uncolor.quiz.results_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.ResultsViewRenderer;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.universal_adapter.ResultsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsActivity extends AppCompatActivity {

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
        initResultsTable();
        int gameType = getIntent().getIntExtra(ARG_GAME_TYPE, 0);
        if(gameType == GAME_TYPE_SINGLE){
            ArrayList<Integer> answers = getIntent().getIntegerArrayListExtra(ARG_ANSWERS);
            ArrayList<Integer> enemyAnswers = getIntent().getIntegerArrayListExtra(ARG_ENEMY_ANSWERS);
            Results results = new Results();
            results.setMyAnswers(answers);
            results.setEnemyAnswers(enemyAnswers);
            adapter.add(results);
            textViewResultScores.setText(adapter.getResultsCounter() + " - " + adapter.getEnemyResultsCounter());
        }

        if(gameType == GAME_TYPE_ONLINE){

        }
    }

    @OnClick(R.id.buttonBack)
    void onBackButtonClick(){
        finish();
    }

    private void initResultsTable(){
        adapter = new ResultsAdapter();
        adapter.registerRenderer(new ResultsViewRenderer(Results.TYPE, this));
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
    }
}
