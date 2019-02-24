package com.sap.uncolor.quiz.results_activity;

import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;

public interface ResultActivityContract {

    interface Presenter {
        void onStartSingleGame();
        void onStartOnlineGame();
        void onUpdateInfoAboutOnlineGame(Room room);
        void onCompleteGame(int mode);
    }

    interface View {
        void showStartGameLoadingDialog();
        void hideStartGameLoadingDialog();
        void showGameInfoLoadingDialog();
        void hideGameInfoLoadingDialog();
        void startSingleGame(Quiz quiz);
        void startOnlineGame();
        void showErrorMessage();
        void updateInfoAboutOnlineGame(Room room);
        void showGameInfoLoadingFailureMessage();
        void gameOver(int mode);
    }

}
