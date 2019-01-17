package com.sap.uncolor.quiz.results_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.ResultsViewRenderer;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultsActivity extends AppCompatActivity {

    private static final String ARG_GAME_TYPE = "game_type";
    private static final String ARG_ANSWERS = "answers";

    private static final int GAME_TYPE_SINGLE = 1;
    private static final int GAME_TYPE_ONLINE = 2;
    private static final int GAME_TYPE_PRIVATE = 3;

    @BindView(R.id.recyclerViewResults)
    RecyclerView recyclerViewResults;

    private UniversalAdapter adapter;


    public static Intent getInstance(Context context){
        return new Intent(context, ResultsActivity.class);
    }

    public static Intent getInstanceForSingleGame(Context context, ArrayList<Integer> answers){
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putIntegerArrayListExtra(ARG_ANSWERS,  answers);
        intent.putExtra(ARG_GAME_TYPE, GAME_TYPE_SINGLE);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        initResultsTable();
        int gameType = getIntent().getIntExtra(ARG_GAME_TYPE, 0);
        if(gameType == GAME_TYPE_SINGLE){
            ArrayList<Integer> answers = getIntent().getIntegerArrayListExtra(ARG_ANSWERS);
            App.Log("answers size: " + answers.size());
            adapter.add(new Results(answers));
        }

    }

    @OnClick(R.id.buttonBack)
    void onBackButtonClick(){
        finish();
    }

    private void initResultsTable(){
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new ResultsViewRenderer(Results.TYPE, this));
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));


    }
}
