package com.sap.uncolor.quiz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sap.uncolor.quiz.models.Quiz;

public class QuizFragmentPagerAdapter extends FragmentPagerAdapter {

    private QuizFragment quizFragmentOne;
    private QuizFragment quizFragmentTwo;
    private QuizFragment quizFragmentThree;

    private AnswerListener answerListener;

    public QuizFragmentPagerAdapter(FragmentManager fm, Quiz model) {
        super(fm);
        quizFragmentOne = QuizFragment.newInstanceForOffline(model, 1);
        quizFragmentTwo = QuizFragment.newInstanceForOffline(model, 2);
        quizFragmentThree = QuizFragment.newInstanceForOffline(model, 3);
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
