package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class TeamResultsViewRenderer extends ViewRenderer<Team, TeamResultsViewHolder> {

    public TeamResultsViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull Team model, @NonNull TeamResultsViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public TeamResultsViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new TeamResultsViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.private_game_results_item, parent,
                false));
    }
}
