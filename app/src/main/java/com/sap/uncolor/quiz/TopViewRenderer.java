package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class TopViewRenderer extends ViewRenderer<User, TopViewHolder> {

    public TopViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull User model, @NonNull TopViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public TopViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new TopViewHolder(LayoutInflater.from(context).inflate(R.layout.top_item, parent,
                false));
    }
}
