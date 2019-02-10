package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.results_activity.ResultsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.textViewEnemyName)
    TextView textViewEnemyName;

    @BindView(R.id.textViewEnemyPoints)
    TextView textViewEnemyPoints;

    @BindView(R.id.imageViewEnemyAvatar)
    CircleImageView imageViewEnemyAvatar;

    @BindView(R.id.textViewCurrentRound)
    TextView textViewCurrentRound;

    private Room room;

    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @SuppressLint("SetTextI18n")
    public void bind(Room room){
        this.room = room;
        itemView.setOnClickListener(this);
        if(room.getRounds() == null){
            textViewCurrentRound.setText("");
        }

        if(room.getCompetitor() == null){
            textViewEnemyName.setText("N/A");
            textViewEnemyPoints.setText("N/A");
            return;
        }

        if(room.isMine()){
            textViewEnemyName.setText(room.getCompetitor().getLogin());
            textViewEnemyPoints.setText(Integer.toString(room.getCompetitor().getPoints()));
        }
        else {
            textViewEnemyName.setText(room.getCreator().getLogin());
            textViewEnemyPoints.setText(Integer.toString(room.getCreator().getPoints()));
        }

        //add Glide loader for avatar
    }

    @Override
    public void onClick(View v) {
        itemView.getContext().startActivity(ResultsActivity
                .getInstanceForOnlineGame(itemView.getContext(), room));
    }
}
