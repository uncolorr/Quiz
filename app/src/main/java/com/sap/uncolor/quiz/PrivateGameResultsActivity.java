package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.models.PrivateGame;
import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.models.request_datas.GetQuestionsRequestData;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.MessageReporter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivateGameResultsActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener{

    public static final int MODE_INIT = 1;
    public static final int MODE_SHOW_RESULTS = 2;

    public static final String ARG_MODE = "mode";
    public static final String ARG_PRIVATE_GAME = "private_game";

    @BindView(R.id.recyclerViewResults)
    RecyclerView recyclerViewResults;

    @BindView(R.id.textViewNextPlayer)
    TextView textViewNextPlayer;


    private UniversalAdapter adapter;

    private AlertDialog loadingDialog;

    private PrivateGame privateGame;

    private int mode;

    public static Intent getInstanceForInitGame(Context context){
        Intent intent = new Intent(context, PrivateGameResultsActivity.class);
        intent.putExtra(ARG_MODE, MODE_INIT);
        return intent;
    }

    public static Intent getInstanceForShowResults(Context context, PrivateGame privateGame){
        Intent intent = new Intent(context, PrivateGameResultsActivity.class);
        intent.putExtra(ARG_MODE, MODE_SHOW_RESULTS);
        intent.putExtra(ARG_PRIVATE_GAME, privateGame);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_game_results);
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new TeamResultsViewRenderer(Team.TYPE, this));
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mode = getIntent().getIntExtra(ARG_MODE, 0);
        if(mode == MODE_INIT){
            DBManager dbManager = new DBManager(this);
            ArrayList<Team> teams = dbManager.getPrivateGameTeamsFromDatabase();
            for (int i = 0; i < teams.size(); i++) {
                adapter.add(teams.get(i));
            }
            dbManager.close();
            privateGame = new PrivateGame(teams);
        }

        if(mode == MODE_SHOW_RESULTS){
            privateGame = (PrivateGame) getIntent().getSerializableExtra(ARG_PRIVATE_GAME);
            ArrayList<Team> teams = privateGame.getTeams();
            for (int i = 0; i < teams.size(); i++) {
                adapter.add(teams.get(i));
            }
        }

        textViewNextPlayer.setText("Отвечает " + privateGame.getCurrentPlayer().getName() +
                ", " + privateGame.getCurrentTeam().getName());
    }

    public void cancelLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.cancel();
        }
    }

    @OnClick(R.id.linearLayoutStartGame)
    void onStartGameButtonClick(){
        loadingDialog.show();
        Api.getSource().getQuestions(new GetQuestionsRequestData())
                .enqueue(ApiResponse.getCallback(getApiQuestionsResponse(), this));
    }

    private ApiResponse.ApiResponseListener<ResponseModel<List<Question>>> getApiQuestionsResponse() {
        return new ApiResponse.ApiResponseListener<ResponseModel<List<Question>>>() {
            @Override
            public void onResponse(ResponseModel<List<Question>> result) {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(PrivateGameResultsActivity.this,
                            "Ошибка",
                            "Ошибка создания игры");
                }
                else {
                    Quiz quiz = new Quiz();
                    quiz.setQuestions(result.getResult());
                    startActivity(QuizActivity.
                            getInstanceForPrivateGame(PrivateGameResultsActivity.this, quiz, privateGame));
                    finish();
                }
            }
        };
    }

    @Override
    public void onFailure(int code, String message) {
        cancelLoadingDialog();
        MessageReporter.showMessage(PrivateGameResultsActivity.this,
                "Ошибка",
                "Ошибка создания игры");
    }
}
