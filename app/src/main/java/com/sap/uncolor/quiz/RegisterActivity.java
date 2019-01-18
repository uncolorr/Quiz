package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    public static Intent getInstance(Context context){
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
