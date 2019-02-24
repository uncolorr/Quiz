package com.sap.uncolor.quiz.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.models.request_datas.GiveUpRequestData;

import java.util.Objects;

public class GiveUpService extends Service {

    public static final String ARG_ROOM_UUID = "room_uuid";

    public static final String ACTION_GIVE_UP = "give_up";

    private static final int TRYING_COUNT_LIMIT = 12;

    private static final int TRYING_COUNT_INTERVAL = 5000;

    private Runnable giveUpRunnable;

    private boolean isGiveUp;

    private String room_uuid;

    private int tryingCount;

    private Handler handler;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        App.Log("Giveup service started");
        super.onCreate();
        handler = new Handler();
        giveUpRunnable = getGiveUpRunnable();
        isGiveUp = false;
        tryingCount = 0;
    }

    private Runnable getGiveUpRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                sendGiveUpRequest();
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.Log("GiveUpService destroyed");
    }

    private boolean isCanTryMore(){
        if(tryingCount >= TRYING_COUNT_LIMIT){
            return false;
        }
        return true;
    }

    private void checkGiveUp(){
        if(!isGiveUp){
            if(isCanTryMore()){
                handler.postDelayed(giveUpRunnable, TRYING_COUNT_INTERVAL);
            }
            else {
                isGiveUp = true;
                stopSelf();
            }
        }
    }

    private void sendGiveUpRequest(){
        App.Log("Trying " + tryingCount + "...");
        Api.getSource().giveUp(new GiveUpRequestData(room_uuid))
                .enqueue(ApiResponse.getCallback(getGiveUpResponseListener(), getGiveUpFailureListener()));
    }

    private ApiResponse.ApiFailureListener getGiveUpFailureListener() {
        return new ApiResponse.ApiFailureListener() {
            @Override
            public void onFailure(int code, String message) {
                tryingCount++;
                checkGiveUp();
            }
        };
    }

    private ApiResponse.ApiResponseListener<ResponseModel<String>> getGiveUpResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<String>>() {
            @Override
            public void onResponse(ResponseModel<String> result) {
                tryingCount++;
                if(result == null || result.getResult() == null){
                    checkGiveUp();
                    return;
                }
                isGiveUp = true;
                stopSelf();
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (Objects.equals(action, ACTION_GIVE_UP)){
                Bundle extras = intent.getExtras();
                if(extras != null) {
                    room_uuid = extras.getString(ARG_ROOM_UUID, "");
                    handler.post(giveUpRunnable);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
