package com.sap.uncolor.quiz.utils;

public class LoadingVideoAdsStateManager {

    public static final int STATE_LOADING = 1;
    public static final int STATE_LOADED = 2;
    public static final int STATE_LOADING_FAILURE = 3;

    private int state;

    public LoadingVideoAdsStateManager() {
        state = STATE_LOADING;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isLoaded(){
        return state == STATE_LOADED;
    }

    public boolean isLoading(){
        return state == STATE_LOADING;
    }

    public boolean isLoadingFailed(){
        return state == STATE_LOADING_FAILURE;
    }
}
