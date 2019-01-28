package com.sap.uncolor.quiz.database;

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
    public static final String DATABASE_NAME = "offline_games";
    public static final String TABLE_SINGLE_GAME = "single_game";


    public static final String TABLE_PRIVATE_GAME_PLAYERS = "private_game_players";
    public static final String TABLE_PRIVATE_GAME_TEAMS = "private_game_teams";

    public static final String FIELD_PLAYER_NAME = "player_name";
    public static final String FIELD_PLAYER_POINTS_RIGHT = "player_points_right";
    public static final String FIELD_PLAYER_POINTS_WRONG = "player_points_wrong";
    public static final String FIELD_PLAYER_TEAM_ID = "player_team_id";

    public static final String FIELD_TEAM_NAME = "team_name";
    public static final String FIELD_TEAM_ID = "id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        App.Log("--- onCreate database ---");
        db.execSQL("create table " + TABLE_SINGLE_GAME +"("
                + FIELD_TEAM_ID + " integer primary key autoincrement,"
                + FIELD_MY_ROUND_ONE + " integer,"
                + FIELD_MY_ROUND_TWO + " integer,"
                + FIELD_MY_ROUND_THREE + " integer,"
                + FIELD_ENEMY_ROUND_ONE + " integer,"
                + FIELD_ENEMY_ROUND_TWO + " integer,"
                + FIELD_ENEMY_ROUND_THREE + " integer"+ ");");

        db.execSQL("create table " + TABLE_PRIVATE_GAME_TEAMS +"("
                + "id integer primary key autoincrement,"
                + FIELD_TEAM_NAME + " text" + ");");

        db.execSQL("create table " + TABLE_PRIVATE_GAME_PLAYERS +"("
                + "id integer primary key autoincrement,"
                + FIELD_PLAYER_NAME + " text,"
                + FIELD_PLAYER_TEAM_ID + " integer,"
                + FIELD_PLAYER_POINTS_RIGHT + " integer,"
                + FIELD_PLAYER_POINTS_WRONG + " integer,"
                + "foreign key ("+ FIELD_PLAYER_TEAM_ID + ")"+" REFERENCES " + TABLE_PRIVATE_GAME_TEAMS + "(id)"
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
