package com.sap.uncolor.quiz;

public interface TimerListener {
    void onTick(int secondsFinished);
    void onFinish();
}
