package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class PlayerViewRenderer extends ViewRenderer<PrivateGamePlayer, PlayerViewHolder> {

    public PlayerViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull PrivateGamePlayer model, @NonNull PlayerViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public PlayerViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new PlayerViewHolder(LayoutInflater.from(context).inflate(R.layout.player_item, parent,
                false));
    }
}