package com.sap.uncolor.quiz;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingDialog {

    public static final String LABEL_LOADING = "Загрузка...";
    public static final String LABEL_PROCESSING = "Обработка...";
    public static final String LABEL_UPDATING = "Обновление...";
    public static final String LABEL_EXIT_FROM_GAME = "Выход из игры...";


    public static AlertDialog newInstanceWithoutCancelable(Context context, String label) {
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_processing, null);
        TextView textViewLabel = view.findViewById(R.id.textViewMessage);
        textViewLabel.setText(label);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.setCancelable(false);
        return dialog;
    }
}
