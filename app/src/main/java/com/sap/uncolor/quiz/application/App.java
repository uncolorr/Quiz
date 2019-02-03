package com.sap.uncolor.quiz.application;

import android.app.Application;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.models.User;

public class App extends Application {

    public static final String KEY_USER = "user";

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
        Api.init();
    }

    public static void Log(String message){
        Log.i("fg", message);
    }

    public static void putUserData(User user){
        Hawk.put(KEY_USER, user);
    }

    public static String getUserName(){
        User user = Hawk.get(KEY_USER);
        return user.getLogin();
    }

    public static int getUserPoints(){
        User user = Hawk.get(KEY_USER);
        return user.getPoints();
    }

    public static String getToken(){
        User user = Hawk.get(KEY_USER);
        return user.getToken();
    }



    public static void logout(){
        Hawk.delete(KEY_USER);
    }

    public static boolean isAuth(){
        return Hawk.contains(KEY_USER);
    }
}
