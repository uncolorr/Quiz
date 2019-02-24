package com.sap.uncolor.quiz.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.results_activity.ResultActivityPresenter;

import de.hdodenhof.circleimageview.CircleImageView;

public class WinnerDialog {

    private AlertDialog.Builder builder;

    private TextView textViewWinner;

    private TextView textViewResults;

    private CircleImageView imageViewWinnerAvatar;

    private Button okButton;

    private Context context;

    private ResultActivityPresenter presenter;

    private int mode;

    private User user;

    private AlertDialog alertDialog;

    public WinnerDialog(Context context, ResultActivityPresenter presenter, int mode) {
        this.context = context;
        this.presenter = presenter;
        this.user = new User();
        this.mode = mode;
        builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_winner, null);
        textViewWinner = view.findViewById(R.id.textViewWinnerName);
        textViewResults = view.findViewById(R.id.textViewResults);
        imageViewWinnerAvatar = view.findViewById(R.id.imageViewWinnerAvatar);
        okButton = view.findViewById(R.id.okButton);
        okButton.setOnClickListener(getOnOkClickListener());
        builder.setCancelable(false);
        builder.setView(view);
        alertDialog = builder.create();
    }

    private View.OnClickListener getOnOkClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCompleteGame(mode);
                cancel();
            }
        };
    }

    public void setUserAsWinner(User user, int userPoints, int enemyPoints){
        this.user = user;
        textViewWinner.setText(user.getLogin());
        textViewResults.setText(userPoints + " - " + enemyPoints);
        uploadAvatar(user, imageViewWinnerAvatar);
    }

    public void setComputerAsWinner(int userPoints, int enemyPoints){
        textViewWinner.setText("Компьютер");
        textViewResults.setText(userPoints + " - " + enemyPoints);
        imageViewWinnerAvatar.setImageResource(R.drawable.ic_monitor);
    }

    public void setDraw(int userPoints, int enemyPoints){
        textViewWinner.setText("Ничья");
        textViewResults.setText(userPoints + " - " + enemyPoints);
        imageViewWinnerAvatar.setImageResource(R.drawable.brain);
    }


    private void uploadAvatar(User user, CircleImageView imageViewAvatar){
        if (user.getAvatar().isEmpty()) {
            if (user.getSex().equals(User.SEX_TYPE_MALE)) {
                imageViewAvatar.setImageResource(R.drawable.boy);
            } else if (user.getSex().equals(User.SEX_TYPE_FEMALE)) {
                imageViewAvatar.setImageResource(R.drawable.girl);
            }
        } else {
            Glide.with(context).load(user.getAvatar()).into(imageViewAvatar);
        }
    }

    public void show(){
        if(alertDialog != null) {
            alertDialog.show();
        }
    }

    public void cancel(){
        if(alertDialog != null) {
            alertDialog.cancel();
        }
    }
}




