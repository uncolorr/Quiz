package com.sap.uncolor.quiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sap.uncolor.quiz.models.Quiz;
import com.sap.uncolor.quiz.models.Room;

public class QuizFragmentPagerAdapter extends FragmentPagerAdapter {

    private QuizFragment quizFragmentOne;
    private QuizFragment quizFragmentTwo;
    private QuizFragment quizFragmentThree;

    private AnswerListener answerListener;

    /*Offline game*/
    public QuizFragmentPagerAdapter(FragmentManager fm, Quiz quiz) {
        super(fm);
        quizFragmentOne = QuizFragment.newInstanceForOffline(quiz, 1);
        quizFragmentTwo = QuizFragment.newInstanceForOffline(quiz, 2);
        quizFragmentThree = QuizFragment.newInstanceForOffline(quiz, 3);

    }

    /*Online game*/
    public QuizFragmentPagerAdapter(FragmentManager fm, Quiz quiz, Room room, int round) {
        super(fm);
        quizFragmentOne = QuizFragment.newInstanceForOnline(quiz, round, room, 0);
        quizFragmentTwo = QuizFragment.newInstanceForOnline(quiz, round, room, 1);
        quizFragmentThree = QuizFragment.newInstanceForOnline(quiz, round, room, 2);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return quizFragmentOne;
            case 1:
                return quizFragmentTwo;
            case 2:
                return quizFragmentThree;

        }
        return null;
    }

    public void disableInterface(int question_index){
        switch (question_index){
            case 0:
                quizFragmentOne.disableInterface();
                break;
            case 1:
                quizFragmentTwo.disableInterface();
                break;
            case 2:
                quizFragmentThree.disableInterface();
                break;
        }
    }

    public void setAnswerListener(AnswerListener answerListener){
        this.answerListener = answerListener;
        quizFragmentOne.setAnswerListener(this.answerListener);
        quizFragmentTwo.setAnswerListener(this.answerListener);
        quizFragmentThree.setAnswerListener(this.answerListener);
    }

    @Override
    public int getCount() {
        return 3;
    }


}
