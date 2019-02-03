package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.universal_adapter.ViewRenderer;

public class RoomViewRenderer extends ViewRenderer<Room, RoomViewHolder> {
    public RoomViewRenderer(int type, Context context) {
        super(type, context);
    }

    @Override
    public void bindView(@NonNull Room model, @NonNull RoomViewHolder holder) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public RoomViewHolder createViewHolder(@Nullable ViewGroup parent) {
        return new RoomViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.room_item, parent,
                        false));
    }
}
