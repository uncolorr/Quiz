package com.sap.uncolor.quiz.results_activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sap.uncolor.quiz.models.Results;

import java.util.ArrayList;

public class DBManager {

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public void addResultToDatabase(ArrayList<Integer> answers, ArrayList<Integer> enemyAnswers){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_MY_ROUND_ONE, answers.get(0));
        cv.put(DBHelper.FIELD_MY_ROUND_TWO, answers.get(1));
        cv.put(DBHelper.FIELD_MY_ROUND_THREE, answers.get(2));
        cv.put(DBHelper.FIELD_ENEMY_ROUND_ONE, enemyAnswers.get(0));
        cv.put(DBHelper.FIELD_ENEMY_ROUND_TWO, enemyAnswers.get(1));
        cv.put(DBHelper.FIELD_ENEMY_ROUND_THREE, enemyAnswers.get(2));
        db.insert(DBHelper.TABLE_NAME, null, cv);
    }

    public ArrayList<Results> getResultsFromDatabase(){

        ArrayList<Results> results = new ArrayList<>();
        Cursor c = db.query(DBHelper.TABLE_NAME, null,
                null,
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            int myRoundOneIndex = c.getColumnIndex(DBHelper.FIELD_MY_ROUND_ONE);
            int myRoundTwoIndex = c.getColumnIndex(DBHelper.FIELD_MY_ROUND_TWO);
            int myRoundThreeIndex = c.getColumnIndex(DBHelper.FIELD_MY_ROUND_THREE);
            int enemyRoundOneIndex = c.getColumnIndex(DBHelper.FIELD_ENEMY_ROUND_ONE);
            int enemyRoundTwoIndex = c.getColumnIndex(DBHelper.FIELD_ENEMY_ROUND_TWO);
            int enemyRoundThreeIndex = c.getColumnIndex(DBHelper.FIELD_ENEMY_ROUND_THREE);

            do {
                Results result = new Results(Results.STATE_COMPLETED);
                ArrayList<Integer> answers = new ArrayList<>();
                ArrayList<Integer> enemyAnswers = new ArrayList<>();

                answers.add(c.getInt(myRoundOneIndex));
                answers.add(c.getInt(myRoundTwoIndex));
                answers.add(c.getInt(myRoundThreeIndex));

                enemyAnswers.add(c.getInt(enemyRoundOneIndex));
                enemyAnswers.add(c.getInt(enemyRoundTwoIndex));
                enemyAnswers.add(c.getInt(enemyRoundThreeIndex));

                result.setMyAnswers(answers);
                result.setEnemyAnswers(enemyAnswers);

                results.add(result);
            } while (c.moveToNext());
        }
        return results;
    }

    public void clearDatabase(){
        db.delete(DBHelper.TABLE_NAME, null, null);
    }

    public int getCompletedRoundsCount(){
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null,
                null,
                null,
                null,
                null,
                null);

        int count = cursor.getCount();
        return count;
    }

    public void close(){
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

}
