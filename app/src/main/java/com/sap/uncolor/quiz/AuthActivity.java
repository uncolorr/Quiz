package com.sap.uncolor.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sap.uncolor.quiz.main_activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonCreateAccount)
    void onCreateAccountButtonClick(){
        startActivity(RegisterActivity.getInstance(this));
    }

    @OnClick(R.id.buttonLogin)
    void onLoginButtonClick(){
        startActivity(MainActivity.getInstance(this));

    }
}
