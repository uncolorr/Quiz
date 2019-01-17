package com.sap.uncolor.quiz.application;

import android.app.Application;
import android.util.Log;

import com.sap.uncolor.quiz.apis.Api;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Api.init();
    }

    public static void Log(String message){
        Log.i("fg", message);
    }
}
