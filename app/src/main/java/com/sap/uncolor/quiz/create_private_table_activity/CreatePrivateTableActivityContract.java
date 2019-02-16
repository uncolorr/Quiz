package com.sap.uncolor.quiz.create_private_table_activity;

public interface CreatePrivateTableActivityContract {

    interface Presenter {
        void onRemoveItem(int index);
    }

    interface View {
        void removeItem(int index);
    }
}
