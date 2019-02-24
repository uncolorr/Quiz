package com.sap.uncolor.quiz.utils;

public class TextFormatter {

    public static String toNormalWinsLosesFormat(int wins, int loses){
        return ("Побед: " + Integer.toString(wins) + ", " + "Поражений: " + Integer.toString(loses));
    }

    public static String toShortWinsLosesFormat(int wins, int loses){
        return ("(" + Integer.toString(wins) + "/" + Integer.toString(loses) + ")");
    }
}
