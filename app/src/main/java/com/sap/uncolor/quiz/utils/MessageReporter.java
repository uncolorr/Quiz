package com.sap.uncolor.quiz.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class MessageReporter {

        public static void showMessage(Context context, String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    public static void showConfirmForExitFromGame(Context context, DialogInterface.OnClickListener exitListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Подверждение выхода из игры");
        builder.setMessage("Вы действительно хотите выйти из игры?");
        builder.setNegativeButton("ОТМЕНА", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("ОК", exitListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
