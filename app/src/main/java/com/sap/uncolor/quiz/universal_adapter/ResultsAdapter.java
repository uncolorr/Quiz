package com.sap.uncolor.quiz.universal_adapter;

import com.sap.uncolor.quiz.models.Results;

import java.util.ArrayList;
import java.util.List;

public class ResultsAdapter extends UniversalAdapter {

    public int getResultsCounter(){
        int resultsCounter = 0;
        List<Results> results = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            results.add((Results) items.get(i));
        }
        for (int i = 0; i < results.size(); i++) {
            resultsCounter += results.get(i).getMyRightAnswersCount();
        }
        return resultsCounter;
    }


    public int getEnemyResultsCounter(){
        int resultsCounter = 0;
        List<Results> results = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            results.add((Results) items.get(i));
        }
        for (int i = 0; i < results.size(); i++) {
            resultsCounter += results.get(i).getEnemyRightAnswersCount();
        }
        return resultsCounter;
    }


}
