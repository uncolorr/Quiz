package com.sap.uncolor.quiz.create_private_table_activity;

public interface CreatePrivateTableActivityContract {

    interface Presenter {
        void onRemoveTeamItem(int index);
        void onRemovePrivatePlayerItem(int index);
    }

    interface View {
        void removeTeamItem(int index);
        void removePlayerItem(int index);
    }
}
