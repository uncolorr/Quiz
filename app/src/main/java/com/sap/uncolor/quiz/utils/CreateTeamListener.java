package com.sap.uncolor.quiz.utils;

import com.sap.uncolor.quiz.models.PrivateGamePlayer;

import java.util.ArrayList;

public interface CreateTeamListener {
    void onCreateTeam(ArrayList<PrivateGamePlayer> privateGamePlayers);
}
