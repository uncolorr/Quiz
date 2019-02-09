package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.Round;
import com.sap.uncolor.quiz.results_activity.ResultActivityPresenter;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class RoundViewRenderer extends ViewRenderer<Round, RoundViewHolder> {
    private ResultActivityPresenter presenter;
    public RoundViewRenderer(int type, Context context, ResultActivityPresenter presenter) {
        super(type, context);
        this.presenter = presenter;

    }

    @Override
    public void bindView(@NonNull Round model, @NonNull RoundViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public RoundViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new RoundViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.results_item, parent,
                        false), presenter);
    }
}
