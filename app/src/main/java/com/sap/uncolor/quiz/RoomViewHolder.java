package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sap.uncolor.quiz.models.Room;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.results_activity.ResultsActivity;
import com.sap.uncolor.quiz.utils.TextFormatter;

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

    @BindView(R.id.textViewWinsAndLoses)
    TextView textViewWinsAndLoses;

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
        else {
            textViewCurrentRound.setText(Integer.toString(room.getRounds().size()) + "-й раунд");
        }

        if(room.getCompetitor() == null){
            textViewEnemyName.setText("N/A");
            textViewEnemyPoints.setText("N/A");
            textViewWinsAndLoses.setText("N/A");
            imageViewEnemyAvatar.setImageResource(android.R.color.white);
            return;
        }

        if(room.isMine()){
            textViewEnemyName.setText(room.getCompetitor().getLogin());
            textViewEnemyPoints.setText(Integer.toString(room.getCompetitor().getPoints()));
            textViewWinsAndLoses.setText(TextFormatter.toShortWinsLosesFormat(
                    room.getCompetitor().getWinsCount(),
                    room.getCompetitor().getLosesCount()));
            uploadAvatar(room.getCompetitor(), imageViewEnemyAvatar);
        }
        else {
            textViewEnemyName.setText(room.getCreator().getLogin());
            textViewEnemyPoints.setText(Integer.toString(room.getCreator().getPoints()));
            textViewWinsAndLoses.setText(TextFormatter.toShortWinsLosesFormat(
                    room.getCreator().getWinsCount(),
                    room.getCreator().getLosesCount()));
            uploadAvatar(room.getCreator(), imageViewEnemyAvatar);
        }
    }

    private void uploadAvatar(User user, CircleImageView imageViewAvatar){
        if (user.getAvatar().isEmpty()) {
            if (user.getSex().equals(User.SEX_TYPE_MALE)) {
                imageViewAvatar.setImageResource(R.drawable.boy);
            } else if (user.getSex().equals(User.SEX_TYPE_FEMALE)) {
                imageViewAvatar.setImageResource(R.drawable.girl);
            }
            return;
        }
        Glide.with(itemView).load(user.getAvatar()).into(imageViewAvatar);
    }

    @Override
    public void onClick(View v) {
        itemView.getContext().startActivity(ResultsActivity
                .getInstanceForOnlineGame(itemView.getContext(), room));
    }
}
