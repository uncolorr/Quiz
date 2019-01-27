package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.PrivateGamePlayer;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class PrivateGamePlayerViewRenderer extends ViewRenderer<PrivateGamePlayer, PrivateGamePlayerViewHolder> {

    public PrivateGamePlayerViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull PrivateGamePlayer model, @NonNull PrivateGamePlayerViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public PrivateGamePlayerViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new PrivateGamePlayerViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.private_game_player_item, parent,
                false));
    }
}
