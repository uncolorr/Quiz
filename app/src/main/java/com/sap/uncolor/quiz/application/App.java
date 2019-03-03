package com.sap.uncolor.quiz.application;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
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
        MobileAds.initialize(this, "ca-app-pub-1541225587417986~7226824885");
    }

    public static void Log(String message){
        if(message == null){
            return;
        }
        if(message.isEmpty()){
            return;
        }
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

    public static void updateUserData(User update){
        User user = getUser();
        user.setAvatar(update.getAvatar());
        user.setPoints(update.getPoints());
        user.setSex(update.getSex());
        user.setWinsCount(update.getWinsCount());
        user.setLosesCount(update.getLosesCount());
        Hawk.put(KEY_USER, user);
    }

    public static User getUser(){
        return Hawk.get(KEY_USER);
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
