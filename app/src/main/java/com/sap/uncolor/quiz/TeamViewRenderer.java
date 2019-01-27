package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

class TeamViewRenderer extends ViewRenderer<Team, TeamViewHolder> {

    public TeamViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull Team model, @NonNull TeamViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public TeamViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new TeamViewHolder(LayoutInflater.from(context).inflate(R.layout.results_item, parent,
                false));
    }
}

