package com.sap.uncolor.quiz.results_activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.sap.uncolor.quiz.LoadingDialog;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.ResultsViewRenderer;
import com.sap.uncolor.quiz.RoundViewRenderer;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.database.DBManager;
import com.sap.uncolor.quiz.dialogs.MessageReporter;
import com.sap.uncolor.quiz.dialogs.WinnerDialog;
import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.Round;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.quiz_activity.QuizActivity;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.LoadingVideoAdsStateManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ResultsActivity extends AppCompatActivity implements ResultActivityContract.View, RewardedVideoAdListener {

    private static final String ARG_GAME_TYPE = "game_type";
    private static final String ARG_ANSWERS = "answers";
    private static final String ARG_ENEMY_ANSWERS = "enemy_answers";
    private static final String ARG_ROOM = "room";

    public static final int GAME_TYPE_SINGLE = 1;
    public static final int GAME_TYPE_ONLINE = 2;

    private static final int UPDATE_RESULTS_INTERVAL = 5000;

    @BindView(R.id.recyclerViewResults)
    RecyclerView recyclerViewResults;

    @BindView(R.id.textViewResultScores)
    TextView textViewResultScores;

    @BindView(R.id.textViewMyUsername)
    TextView textViewMyUsername;

    @BindView(R.id.textViewEnemyUsername)
    TextView textViewEnemyUsername;

    @BindView(R.id.imageViewMyAvatar)
    CircleImageView imageViewMyAvatar;

    @BindView(R.id.imageViewEnemyAvatar)
    CircleImageView imageViewEnemyAvatar;

    private RewardedVideoAd rewardedVideoAd;

    private UniversalAdapter adapter;

    private DBManager dbManager;

    private ResultActivityPresenter presenter;

    private AlertDialog startGameLoadingDialog;

    private AlertDialog gameInfoLoadingDialog;

    private AlertDialog startLoadingVideoAdsLoadingDialog;

    private Room room;

    private Handler handler;

    private Runnable updateOnlineGameInfoRunnable;

    private int gameType;

    private LoadingVideoAdsStateManager loadingVideoAdsStateManager;

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
        loadingVideoAdsStateManager = new LoadingVideoAdsStateManager();
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        startGameLoadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        gameInfoLoadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        startLoadingVideoAdsLoadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
        presenter = new ResultActivityPresenter(this, this);
        dbManager = new DBManager(this);
        initResultsTable();
        gameType = getIntent().getIntExtra(ARG_GAME_TYPE, 0);
        if (gameType == GAME_TYPE_SINGLE) {
            ArrayList<Integer> answers = getIntent().getIntegerArrayListExtra(ARG_ANSWERS);
            ArrayList<Integer> enemyAnswers = getIntent().getIntegerArrayListExtra(ARG_ENEMY_ANSWERS);
            configInfoAboutSingleGame(answers, enemyAnswers);
        }

        if (gameType == GAME_TYPE_ONLINE) {
            showGameInfoLoadingDialog();
            room = (Room) getIntent().getSerializableExtra(ARG_ROOM);
            presenter.onUpdateInfoAboutOnlineGame(room);
        }
        loadRewardedVideoAd();
    }

    private void configInfoAboutSingleGame(ArrayList<Integer> answers, ArrayList<Integer> enemyAnswers){
        dbManager.addSingleGameResultsToDatabase(answers, enemyAnswers);
        ArrayList<Results> results = dbManager.getSingleGameResultsFromDatabase();
        for (int i = 0; i < results.size(); i++) {
            adapter.add(results.get(i));
        }
        if (dbManager.getCompletedRoundsCount() < 5) {
            adapter.add(new Results(Results.STATE_NEXT_GAME));
        } else {
            showWinnerInSingleGame();
        }
        showUsersInfoForSingleGame(App.getUser());
        countResultsForSingleGame();
    }

    private void showWinnerInSingleGame() {
        FlurryAgent.logEvent("Завершенных одиночных игр");
        WinnerDialog winnerDialog = new WinnerDialog(this, presenter, gameType);
        if(getMyResultsCounter() > getEnemyResultsCounter()){
            winnerDialog.setUserAsWinner(App.getUser(), getMyResultsCounter(), getEnemyResultsCounter());
        }
        else if(getMyResultsCounter() < getEnemyResultsCounter()){
           winnerDialog.setComputerAsWinner(getMyResultsCounter(), getEnemyResultsCounter());
        }
        else {
           winnerDialog.setDraw(getMyResultsCounter(), getEnemyResultsCounter());
        }
        winnerDialog.show();
    }

    private void showWinnerInOnlineGame() {
        FlurryAgent.logEvent("Завершенных онлайн-игр");
        WinnerDialog winnerDialog = new WinnerDialog(this, presenter, gameType);
        if(room.getWinnerId() == Room.WINNER_DRAW_ID){
            winnerDialog.setDraw(getMyRoundPointsCounter(), getEnemyRoundPointsCounter());
            winnerDialog.show();
        }
        if(room.getWinnerId() != Room.WINNER_NONE){
            if(room.getWinnerId() == room.getCreator().getId()){
                winnerDialog.setUserAsWinner(room.getCreator(), getMyRoundPointsCounter(), getEnemyRoundPointsCounter());
            }
            if(room.getCompetitor() != null) {
                if (room.getWinnerId() == room.getCompetitor().getId()) {
                    winnerDialog.setUserAsWinner(room.getCreator(), getMyRoundPointsCounter(), getEnemyRoundPointsCounter());
                }
            }
            winnerDialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showUsersInfoForSingleGame(User user){
        if(gameType == GAME_TYPE_SINGLE) {
            textViewMyUsername.setText(user.getLogin());
            uploadAvatar(user, imageViewMyAvatar);
            textViewEnemyUsername.setText("Компьютер");
            imageViewEnemyAvatar.setImageResource(R.drawable.ic_monitor);
        }
    }

    private void uploadAvatar(User user, CircleImageView imageViewAvatar){
        if (user.getAvatar().isEmpty()) {
            if (user.getSex().equals(User.SEX_TYPE_MALE)) {
                imageViewAvatar.setImageResource(R.drawable.boy);
            } else if (user.getSex().equals(User.SEX_TYPE_FEMALE)) {
                imageViewAvatar.setImageResource(R.drawable.girl);
            }
        } else {
            Glide.with(getApplicationContext()).load(user.getAvatar()).into(imageViewAvatar);
        }
    }

    private int getMyResultsCounter(){
        int resultsCounter = 0;
        List<Results> results = new ArrayList<>();
        for (int i = 0; i < adapter.getItems().size(); i++) {
            results.add((Results) adapter.getItems().get(i));
        }
        for (int i = 0; i < results.size(); i++) {
            if(results.get(i) != null) {
                resultsCounter += results.get(i).getMyRightAnswersCount();
            }
        }
        return resultsCounter;
    }


    private int getEnemyResultsCounter(){
        int enemyResultsCounter = 0;
        List<Results> results = new ArrayList<>();
        for (int i = 0; i < adapter.getItems().size(); i++) {
            results.add((Results) adapter.getItems().get(i));
        }
        for (int i = 0; i < results.size(); i++) {
            if(results.get(i) != null) {
                enemyResultsCounter += results.get(i).getEnemyRightAnswersCount();
            }
        }
        return enemyResultsCounter;
    }

    private int getMyRoundPointsCounter(){
        int pointsCounter = 0;
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < adapter.getItems().size(); i++) {
            rounds.add((Round) adapter.getItems().get(i));
        }
        for (int i = 0; i < rounds.size(); i++) {
            if(rounds.get(i) != null) {
                pointsCounter += rounds.get(i).getMyRightsAnswersCount();
            }
        }
        return pointsCounter;
    }


    private int getEnemyRoundPointsCounter(){
        int enemyPointsCounter = 0;
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < adapter.getItems().size(); i++) {
            rounds.add((Round) adapter.getItems().get(i));
        }
        for (int i = 0; i < rounds.size(); i++) {
            if(rounds.get(i) != null) {
                enemyPointsCounter += rounds.get(i).getEnemyRightsAnswersCount();
            }
        }
        return enemyPointsCounter;
    }

    private void countResultsForSingleGame(){
        textViewResultScores.setText(getMyResultsCounter() + " - " + getEnemyResultsCounter());
    }

    private void countPointsForOnlineGame(){
        textViewResultScores.setText(getMyRoundPointsCounter() + " - " + getEnemyRoundPointsCounter());
    }

    @Override
    protected void onResume() {
        rewardedVideoAd.resume(this);
        super.onResume();
        if (gameType == GAME_TYPE_ONLINE) {
            handler = new Handler();
            updateOnlineGameInfoRunnable = new Runnable() {
                @SuppressLint("StaticFieldLeak")
                @Override
                public void run() {
                    presenter.onUpdateInfoAboutOnlineGame(room);
                    handler.postDelayed(updateOnlineGameInfoRunnable, UPDATE_RESULTS_INTERVAL);
                }
            };
            handler.postDelayed(updateOnlineGameInfoRunnable, UPDATE_RESULTS_INTERVAL);
        }
    }


    @OnClick(R.id.imageButtonBack)
    void onBackButtonClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        rewardedVideoAd.destroy(this);
        super.onDestroy();
        switch (gameType) {
            case GAME_TYPE_SINGLE:
                if(dbManager.isOpen()) {
                    if (dbManager.getCompletedRoundsCount() >= 5) {
                        dbManager.clearSingleGameResults();
                    }
                    dbManager.close();
                }
                break;
            case GAME_TYPE_ONLINE:
                handler.removeCallbacks(updateOnlineGameInfoRunnable);
                break;
        }

    }

    private void initResultsTable() {
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new ResultsViewRenderer(Results.TYPE, this, presenter));
        adapter.registerRenderer(new RoundViewRenderer(Round.TYPE, this, presenter));
        recyclerViewResults.setAdapter(adapter);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showGameInfoLoadingDialog() {
        gameInfoLoadingDialog.show();
    }

    @Override
    public void hideGameInfoLoadingDialog() {
        gameInfoLoadingDialog.cancel();
    }

    @Override
    public void showStartGameLoadingDialog() {
        startGameLoadingDialog.show();
    }

    @Override
    public void hideStartGameLoadingDialog() {
        startGameLoadingDialog.cancel();
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
        startGameLoadingDialog.show();
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
        this.room = room;
        if (room.getCompetitor() == null) {
            textViewMyUsername.setText(room.getCreator().getLogin());
            textViewEnemyUsername.setText("N/A");
            return;
        }

        if(room.isMine()) {
            textViewMyUsername.setText(room.getCreator().getLogin());
            uploadAvatar(room.getCreator(), imageViewMyAvatar);
            textViewEnemyUsername.setText(room.getCompetitor().getLogin());
            uploadAvatar(room.getCompetitor(), imageViewEnemyAvatar);
        } else  {
            textViewMyUsername.setText(room.getCompetitor().getLogin());
            uploadAvatar(room.getCompetitor(), imageViewMyAvatar);
            textViewEnemyUsername.setText(room.getCreator().getLogin());
            uploadAvatar(room.getCreator(), imageViewEnemyAvatar);
        }

        if (room.getRounds() == null) {
            return;
        }

        adapter.clear();
        for (int i = 0; i < room.getRounds().size(); i++) {
            adapter.add(room.getRounds().get(i));
            if (room.isMine()) {
                room.getRounds().get(i).setMine(true);
            } else {
                room.getRounds().get(i).setMine(false);
            }
        }
        countPointsForOnlineGame();
        showWinnerInOnlineGame();
    }

    @Override
    public void showGameInfoLoadingFailureMessage() {
        MessageReporter.showGameInfoLoadingFailureMessage(this, getExitAfterFailureListener());
    }

    @Override
    public void gameOver(int mode) {
        switch (mode) {
            case GAME_TYPE_SINGLE:
                if(dbManager.isOpen()) {
                    if (dbManager.getCompletedRoundsCount() >= 5) {
                        dbManager.clearSingleGameResults();
                    }
                }
                dbManager.close();

                break;
            case GAME_TYPE_ONLINE:
                handler.removeCallbacks(updateOnlineGameInfoRunnable);
                break;
        }


        if(loadingVideoAdsStateManager.isLoadingFailed()){
            finish();
        }
        else if(loadingVideoAdsStateManager.isLoading()){
            startLoadingVideoAdsLoadingDialog.show();
        }
        else if(loadingVideoAdsStateManager.isLoaded()){
            rewardedVideoAd.show();
        }


    }

    private DialogInterface.OnClickListener getExitAfterFailureListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
    }

    @Override
    public void onRewarded(RewardItem reward) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        finish();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
    //    Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
        if(startLoadingVideoAdsLoadingDialog.isShowing()){
            if(loadingVideoAdsStateManager.isLoadingFailed()){
                finish();
            }
        }
        loadingVideoAdsStateManager.setState(LoadingVideoAdsStateManager.STATE_LOADING_FAILURE);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
     //   Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if(startLoadingVideoAdsLoadingDialog.isShowing()){
            startLoadingVideoAdsLoadingDialog.cancel();
            rewardedVideoAd.show();
        }
        loadingVideoAdsStateManager.setState(LoadingVideoAdsStateManager.STATE_LOADED);
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    private void loadRewardedVideoAd() {
        rewardedVideoAd.loadAd("ca-app-pub-1541225587417986/6736666452",
                new AdRequest.Builder().build());
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(this);
        super.onPause();
    }



}
