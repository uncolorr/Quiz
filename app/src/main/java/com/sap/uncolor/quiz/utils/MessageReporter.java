package com.sap.uncolor.quiz.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;

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

    public static void showConfirmForLogout(Context context, DialogInterface.OnClickListener exitListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Подверждение выхода");
        builder.setMessage("Вы действительно хотите выйти из своего аккаунта?");
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

    public static void showMessageWithInfo(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        } else {
            builder.setMessage(Html.fromHtml(message));
        }
        builder.setCancelable(false);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
