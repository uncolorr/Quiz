package com.sap.uncolor.quiz.create_private_table_activity;

import android.content.Context;

public class CreatePrivateTableActivityPresenter implements CreatePrivateTableActivityContract.Presenter{

    private Context context;
    private CreatePrivateTableActivityContract.View view;

    public CreatePrivateTableActivityPresenter(Context context, CreatePrivateTableActivityContract.View view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public void onRemoveTeamItem(int index) {
        view.removeTeamItem(index);
    }

    @Override
    public void onRemovePrivatePlayerItem(int index) {
        view.removePlayerItem(index);
    }


}
