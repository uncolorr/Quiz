package com.sap.uncolor.quiz.dialogs;

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

    public static void showMessage(Context context, String title, String message,
                                   DialogInterface.OnClickListener okListener, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(isCancelable);
        builder.setNeutralButton("OK", okListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showConfirmForExitFromSingleGame(Context context, DialogInterface.OnClickListener exitListener) {
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

    public static void showConfirmForExitFromOnlineGame(Context context, DialogInterface.OnClickListener exitListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Подверждение выхода из игры");
        builder.setMessage("Вы действительно хотите выйти из игры? Текущая игра будет автоматически проиграна!");
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

    public static void showMessageAboutWinInPrivateGame(Context context, String winnerName,
                                                        DialogInterface.OnClickListener exitListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Конец игры");
        builder.setMessage("Победила команда " + winnerName);
        builder.setCancelable(false);
        builder.setPositiveButton("ВЫЙТИ", exitListener);
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

    public static void showGameInfoLoadingFailureMessage(Context context, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ошибка");
        builder.setMessage("Не удалось загрузить данные о поединке");
        builder.setCancelable(false);
        builder.setNeutralButton("OK", okListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
