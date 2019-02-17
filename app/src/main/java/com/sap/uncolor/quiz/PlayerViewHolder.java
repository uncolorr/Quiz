package com.sap.uncolor.quiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sap.uncolor.quiz.create_private_table_activity.CreatePrivateTableActivityPresenter;
import com.sap.uncolor.quiz.models.PrivateGamePlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewPlayerName)
    TextView textViewPlayerName;

    private CreatePrivateTableActivityPresenter presenter;

    public PlayerViewHolder(@NonNull View itemView, CreatePrivateTableActivityPresenter presenter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.presenter = presenter;
    }

    public void bind(PrivateGamePlayer privateGamePlayer){
        textViewPlayerName.setText(privateGamePlayer.getName());
    }

    @OnClick(R.id.imageButtonRemove)
    void onImageButtonRemoveClick(){
        presenter.onRemovePrivatePlayerItem(getAdapterPosition());
    }
}
