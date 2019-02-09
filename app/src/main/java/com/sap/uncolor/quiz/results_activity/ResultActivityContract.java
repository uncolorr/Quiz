package com.sap.uncolor.quiz.results_activity;

import com.sap.uncolor.quiz.models.Quiz;

public interface ResultActivityContract {

    interface Presenter {
        void onStartSingleGame();
        void onStartOnlineGame();
    }

    interface View {
        void showProcessingDialog();
        void hideProcessingDialog();
        void startSingleGame(Quiz quiz);
        void startOnlineGame();
        void showErrorMessage();
    }

}
