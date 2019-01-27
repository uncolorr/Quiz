package com.sap.uncolor.quiz.utils;

import com.sap.uncolor.quiz.models.Player;

import java.util.ArrayList;

public interface CreateTeamListener {
    void onCreateTeam(ArrayList<Player> players);
}
