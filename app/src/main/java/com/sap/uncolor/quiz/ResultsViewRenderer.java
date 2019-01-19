package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.Results;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class ResultsViewRenderer extends ViewRenderer<Results, ResultsViewHolder> {


    public ResultsViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull Results model, @NonNull ResultsViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ResultsViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new ResultsViewHolder(LayoutInflater.from(context).inflate(R.layout.results_item, parent,
                false));
    }
}
