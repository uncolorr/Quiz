package com.sap.uncolor.quiz;

import android.app.Application;
import android.util.Log;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void Log(String message){
        Log.i("fg", message);
    }
}
