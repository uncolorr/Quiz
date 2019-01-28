package com.sap.uncolor.quiz.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.models.Team;

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

    public void addSingleGameResultsToDatabase(ArrayList<Integer> answers, ArrayList<Integer> enemyAnswers){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.FIELD_MY_ROUND_ONE, answers.get(0));
        cv.put(DBHelper.FIELD_MY_ROUND_TWO, answers.get(1));
        cv.put(DBHelper.FIELD_MY_ROUND_THREE, answers.get(2));
        cv.put(DBHelper.FIELD_ENEMY_ROUND_ONE, enemyAnswers.get(0));
        cv.put(DBHelper.FIELD_ENEMY_ROUND_TWO, enemyAnswers.get(1));
        cv.put(DBHelper.FIELD_ENEMY_ROUND_THREE, enemyAnswers.get(2));
        db.insert(DBHelper.TABLE_SINGLE_GAME, null, cv);
    }

    public ArrayList<Team> getPrivateGameTeamsFromDatabase(){
        ArrayList<Team> teams = new ArrayList<>();
        Cursor teamCursor = db.query(DBHelper.TABLE_PRIVATE_GAME_TEAMS, null,
                null,
                null,
                null,
                null,
                null);

        if (teamCursor.moveToFirst()) {
            int idIndex = teamCursor.getColumnIndex("id");
            int teamNameIndex = teamCursor.getColumnIndex(DBHelper.FIELD_TEAM_NAME);
            do {
                Team team = new Team();
                team.setId(teamCursor.getInt(idIndex));
                team.setName(teamCursor.getString(teamNameIndex));

                /*Cursor playersCursor = db.query(DBHelper.TABLE_PRIVATE_GAME_PLAYERS, null,
                        DBHelper.FIELD_PLAYER_TEAM_ID + " = " + Integer.toString(team.getId()),
                        null,
                        null,
                        null,
                        null);

                if (playersCursor.moveToFirst()) {
                    ArrayList<PrivateGamePlayer> players = new ArrayList<>();
                    int playerNameIndex = playersCursor.getColumnIndex(DBHelper.FIELD_PLAYER_NAME);
                    do {
                        PrivateGamePlayer privateGamePlayer = new PrivateGamePlayer(playersCursor.getString(playerNameIndex));
                        players.add(privateGamePlayer);

                    } while (teamCursor.moveToNext());
                    App.Log("players count in team: " + players.size());
                    team.setPrivateGamePlayers(players);
                }*/

                teams.add(team);
                App.Log("team added");

            } while (teamCursor.moveToNext());
            teamCursor.close();
        }

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            Cursor playersCursor = db.query(DBHelper.TABLE_PRIVATE_GAME_PLAYERS, null,
                    DBHelper.FIELD_PLAYER_TEAM_ID + " = " + Integer.toString(team.getId()),
                    null,
                    null,
                    null,
                    null);

            if (playersCursor.moveToFirst()) {
                ArrayList<PrivateGamePlayer> players = new ArrayList<>();
                int playerNameIndex = playersCursor.getColumnIndex(DBHelper.FIELD_PLAYER_NAME);
                do {
                    PrivateGamePlayer privateGamePlayer = new PrivateGamePlayer(playersCursor.getString(playerNameIndex));
                    players.add(privateGamePlayer);

                } while (teamCursor.moveToNext());
                App.Log("players count in team: " + players.size());
                team.setPrivateGamePlayers(players);
            }
            playersCursor.close();
        }

        return teams;
    }

    public ArrayList<Results> getSingleGameResultsFromDatabase() {

        ArrayList<Results> results = new ArrayList<>();
        Cursor c = db.query(DBHelper.TABLE_SINGLE_GAME, null,
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


    public void clearSingleGameResults(){
        db.delete(DBHelper.TABLE_SINGLE_GAME, null, null);
    }

    public void clearPrivateGameResults(){
        db.delete(DBHelper.TABLE_PRIVATE_GAME_PLAYERS, null, null);
        db.delete(DBHelper.TABLE_PRIVATE_GAME_TEAMS, null, null);
    }

    public int getCompletedRoundsCount(){
        Cursor cursor = db.query(DBHelper.TABLE_SINGLE_GAME, null,
                null,
                null,
                null,
                null,
                null);
        return cursor.getCount();
    }

    public void addPrivateGameTeamsFromDatabase(ArrayList<Team> teams){
        clearPrivateGameResults();
        App.Log("team size in manager:" + teams.size());
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            ContentValues cvForTeam = new ContentValues();
            cvForTeam.put(DBHelper.FIELD_TEAM_ID, team.getId());
            cvForTeam.put(DBHelper.FIELD_TEAM_NAME, team.getName());
            db.insert(DBHelper.TABLE_PRIVATE_GAME_TEAMS, null, cvForTeam);
            for (int j = 0; j < team.getPrivateGamePlayers().size(); j++) {
                PrivateGamePlayer player = team.getPrivateGamePlayers().get(j);
                ContentValues cvForPlayer = new ContentValues();
                cvForPlayer.put(DBHelper.FIELD_PLAYER_NAME, player.getName());
                cvForPlayer.put(DBHelper.FIELD_PLAYER_POINTS_RIGHT, player.getPointsRight());
                cvForPlayer.put(DBHelper.FIELD_PLAYER_POINTS_WRONG, player.getPointsWrong());
                cvForPlayer.put(DBHelper.FIELD_PLAYER_TEAM_ID, team.getId());
                db.insert(DBHelper.TABLE_PRIVATE_GAME_PLAYERS, null, cvForPlayer);
            }
        }
    }

    public void close(){
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

}
