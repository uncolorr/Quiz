package com.sap.uncolor.quiz;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sap.uncolor.quiz.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TopViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textViewName)
    TextView textViewName;

    @BindView(R.id.textViewPoints)
    TextView textViewPoints;

    @BindView(R.id.imageViewAvatar)
    CircleImageView imageViewAvatar;

    public TopViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

    @SuppressLint("SetTextI18n")
    public void bind(User user){
        textViewName.setText(user.getLogin());
        textViewPoints.setText(Integer.toString(user.getPoints()));
        if (user.getAvatar().isEmpty()) {
            if (user.getSex().equals(User.SEX_TYPE_MALE)) {
                imageViewAvatar.setImageResource(R.drawable.boy);
            } else if (user.getSex().equals(User.SEX_TYPE_FEMALE)) {
                imageViewAvatar.setImageResource(R.drawable.girl);
            }
        } else {
            Glide.with(itemView).load(user.getAvatar()).into(imageViewAvatar);
        }
    }
}
