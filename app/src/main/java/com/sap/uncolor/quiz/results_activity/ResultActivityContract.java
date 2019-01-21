package com.sap.uncolor.quiz.results_activity;

import com.sap.uncolor.quiz.models.Quiz;

public interface ResultActivityContract {

    interface Presenter{
        void onStartGame();
    }

    interface View{
        void showProcessingDialog();
        void hideProcessingDialog();
        void startGame(Quiz quiz);
        void showErrorMessage();
    }

}
