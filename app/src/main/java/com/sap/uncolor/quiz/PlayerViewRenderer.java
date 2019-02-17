package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.create_private_table_activity.CreatePrivateTableActivityPresenter;
import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class PlayerViewRenderer extends ViewRenderer<PrivateGamePlayer, PlayerViewHolder> {

    private CreatePrivateTableActivityPresenter presenter;

    public PlayerViewRenderer(int type, Context context, CreatePrivateTableActivityPresenter presenter) {
        super(type, context);
        this.presenter = presenter;
    }

    @Override
    public void bindView(@NonNull PrivateGamePlayer model, @NonNull PlayerViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public PlayerViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new PlayerViewHolder(LayoutInflater.from(context).inflate(R.layout.player_item, parent,
                false), presenter);
    }
}