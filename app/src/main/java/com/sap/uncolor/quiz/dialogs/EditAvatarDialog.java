package com.sap.uncolor.quiz.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.sap.uncolor.quiz.R;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditAvatarDialog {

    private CircleImageView circleImageView;

    private Button buttonUploadNewAvatar;
    private Button buttonRemoveAvatar;

    private Context context;

    private AlertDialog dialog;

    public EditAvatarDialog(Context context) {
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(inflater != null) {
            View view = inflater.inflate(R.layout.dialog_edit_avatar, null);
            circleImageView = view.findViewById(R.id.imageViewAvatar);
            buttonUploadNewAvatar = view.findViewById(R.id.buttonUploadNewAvatar);
            buttonRemoveAvatar = view.findViewById(R.id.buttonRemoveAvatar);
            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            uploadAvatar(App.getUser().getAvatar());
        }
        else {
            throw new RuntimeException("Layout inflater in EditAvatarDialog is null");
        }
    }

    public void setOnUploadAvatarClickListener(View.OnClickListener listener){
        buttonUploadNewAvatar.setOnClickListener(listener);
    }

    public void setOnRemoveClickListener(View.OnClickListener listener){
        buttonRemoveAvatar.setOnClickListener(listener);
    }

    public void show(){
        if(dialog != null){
            dialog.show();
        }
    }

    public void cancel(){
        if(dialog != null){
            dialog.cancel();
        }
    }

    public void uploadAvatar(String avatarUrl) {
        if (avatarUrl.isEmpty()) {
            if (App.getUser().getSex().equals(User.SEX_TYPE_MALE)) {
                circleImageView.setImageResource(R.drawable.boy);
            } else if (App.getUser().getSex().equals(User.SEX_TYPE_FEMALE)) {
                circleImageView.setImageResource(R.drawable.girl);
            }
        } else {
            Glide.with(context).load(avatarUrl).into(circleImageView);
        }
    }
}
