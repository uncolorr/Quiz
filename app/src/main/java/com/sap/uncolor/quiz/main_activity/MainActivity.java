package com.sap.uncolor.quiz.main_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.sap.uncolor.quiz.AuthActivity;
import com.sap.uncolor.quiz.CreatePrivateTableActivity;
import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.RoomViewRenderer;
import com.sap.uncolor.quiz.TopActivity;
import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.models.Question;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.request_datas.GetQuestionsRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomRequestData;
import com.sap.uncolor.quiz.models.request_datas.GetRoomsRequestData;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.results_activity.ResultsActivity;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.MessageReporter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener {

    @BindView(R.id.buttonSingleGame)
    Button buttonSingleGame;

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.textViewPoints)
    TextView textViewPoints;

    @BindView(R.id.recyclerViewCurrentGames)
    RecyclerView recyclerViewCurrentGames;

    private AlertDialog loadingDialog;

    private UniversalAdapter adapter;


    public static Intent getInstance(Context context){
        return new Intent(context, MainActivity.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        textViewName.setText(App.getUserName());
        textViewPoints.setText(Integer.toString(App.getUserPoints()));
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new RoomViewRenderer(Room.TYPE, this));
        recyclerViewCurrentGames.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCurrentGames.setAdapter(adapter);
    }

    @OnClick(R.id.buttonSingleGame)
    void onButtonSingleGameClick(){
        DBManager dbManager = new DBManager(this);
        if(dbManager.getCompletedRoundsCount() >= 5){
            dbManager.clearSingleGameResults();
            dbManager.close();
        }
        showLoadingDialog();
        Api.getSource().getQuestions(new GetQuestionsRequestData())
                .enqueue(ApiResponse.getCallback(getApiResponseListener(), this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Api.getSource().getRooms(new GetRoomsRequestData())
                .enqueue(ApiResponse.getCallback(getRoomsResponseListener(), this));
    }

    private ApiResponse.ApiResponseListener<ResponseModel<List<Room>>> getRoomsResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<List<Room>>>() {
            @Override
            public void onResponse(ResponseModel<List<Room>> result) {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка получения списка текущих поединков");
                }
                else {
                    adapter.clear();
                    List<Room> rooms = result.getResult();
                    for (int i = 0; i < rooms.size(); i++) {
                        adapter.add(rooms.get(i));
                    }
                }
            }
        };
    }

    @OnClick(R.id.imageButtonSettings)
    void onSettingsButtonClick(){

    }

    @OnClick(R.id.imageButtonTop)
    void onTopButtonClick(){
        startActivity(TopActivity.getInstance(this));
    }

    @OnClick(R.id.imageButtonExit)
    void onExitButtonClick(){
        App.logout();
        finish();
        startActivity(AuthActivity.getInstance(this));
    }

    @OnClick(R.id.buttonPrivateGame)
    void onButtonPrivateGameClick(){
        startActivity(CreatePrivateTableActivity.getInstance(this));
    }

    @OnClick(R.id.buttonOnlineGame)
    void onButtonOnlineGameClick(){
        showLoadingDialog();
        Api.getSource().getRoom(new GetRoomRequestData())
                .enqueue(ApiResponse.getCallback(getFindRoomResponseListener(), this));
    }

    private ApiResponse.ApiResponseListener<ResponseModel<Room>> getFindRoomResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<Room>>() {
            @Override
            public void onResponse(ResponseModel<Room> result) {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка создания игры");
                }
                else {
                    Room room = result.getResult();
                    startActivity(ResultsActivity
                            .getInstanceForOnlineGame(MainActivity.this, room));
                }
            }
        };
    }


    private ApiResponse.ApiResponseListener<ResponseModel<List<Question>>> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<List<Question>>>() {
            @Override
            public void onResponse(ResponseModel<List<Question>> result) {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(MainActivity.this,
                            "Ошибка",
                            "Ошибка создания игры");
                }
                else {
                    Quiz quiz = new Quiz();
                    quiz.setQuestions(result.getResult());
                    startActivity(QuizActivity.getInstanceForSingleGame(MainActivity.this, quiz));
                }
            }
        };
    }

    public void showLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.show();
        }
    }

    public void cancelLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.cancel();
        }
    }

    @Override
    public void onFailure(int code, String message) {
        App.Log("onFailure");
        cancelLoadingDialog();
        MessageReporter.showMessage(MainActivity.this,
                "Ошибка",
                "Ошибка создания игры");
    }
}
