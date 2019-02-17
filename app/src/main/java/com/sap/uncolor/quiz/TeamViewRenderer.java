package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.create_private_table_activity.CreatePrivateTableActivityContract;
import com.sap.uncolor.quiz.models.Team;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class TeamViewRenderer extends ViewRenderer<Team, TeamViewHolder> {


    private final CreatePrivateTableActivityContract.Presenter presenter;

    public TeamViewRenderer(int type, Context context, CreatePrivateTableActivityContract.Presenter presenter) {
        super(type, context);
        this.presenter = presenter;
    }

    @Override
    public void bindView(@NonNull Team model, @NonNull TeamViewHolder holder) {
        holder.bind(model);

    }

    @NonNull
    @Override
    public TeamViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new TeamViewHolder(LayoutInflater.from(context).inflate(R.layout.team_item, parent,
                false), presenter);
    }
}

