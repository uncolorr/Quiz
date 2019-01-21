package com.sap.uncolor.quiz.results_activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sap.uncolor.quiz.application.App;

public class DBHelper extends SQLiteOpenHelper {

    public static final String FIELD_MY_ROUND_ONE = "my_round_one";
    public static final String FIELD_MY_ROUND_TWO = "my_round_two";
    public static final String FIELD_MY_ROUND_THREE = "my_round_three";
    public static final String FIELD_ENEMY_ROUND_ONE = "enemy_round_one";
    public static final String FIELD_ENEMY_ROUND_TWO = "enemy_round_two";
    public static final String FIELD_ENEMY_ROUND_THREE = "enemy_round_three";
    public static final String DATABASE_NAME = "single_game";
    public static final String TABLE_NAME = "quiz_results";


    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        App.Log("--- onCreate database ---");
        db.execSQL("create table " + TABLE_NAME +"("
                + "id integer primary key autoincrement,"
                + FIELD_MY_ROUND_ONE + " integer,"
                + FIELD_MY_ROUND_TWO + " integer,"
                + FIELD_MY_ROUND_THREE + " integer,"
                + FIELD_ENEMY_ROUND_ONE + " integer,"
                + FIELD_ENEMY_ROUND_TWO + " integer,"
                + FIELD_ENEMY_ROUND_THREE + " integer"+ ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
