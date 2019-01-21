package com.sap.uncolor.quiz;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.main_activity.MainActivity;
import com.sap.uncolor.quiz.models.AuthResponse;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.utils.MessageReporter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener{

    @BindView(R.id.editTextLogin)
    EditText editTextLogin;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if(App.isAuth()){
            startActivity(MainActivity.getInstance(this));
            return;
        }
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);

    }

    @OnClick(R.id.buttonCreateAccount)
    void onCreateAccountButtonClick(){
        startActivity(RegisterActivity.getInstance(this));
    }

    @OnClick(R.id.buttonLogin)
    void onLoginButtonClick(){
        loadingDialog.show();
        Api.getSource().login(editTextLogin.getText().toString(),
                editTextPassword.getText().toString())
                .enqueue(ApiResponse.getCallback(getLoginResponseListener(), this));
    }

    private ApiResponse.ApiResponseListener<AuthResponse> getLoginResponseListener() {
        return new ApiResponse.ApiResponseListener<AuthResponse>() {
            @Override
            public void onResponse(AuthResponse result) {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(AuthActivity.this,
                            "Ошибка",
                            "Ошибка авторизации");
                    return;
                }



                if(result.getResponse().getError() != null){
                    App.Log("error");
                    MessageReporter.showMessage(AuthActivity.this,
                                "Ошибка",
                                "Нет логина или пароля");
                    return;
                }
                    User user = result.getResponse();
                    App.putUserData(user);
                    startActivity(MainActivity.getInstance(AuthActivity.this));
                    finish();
            }
        };
    }

    public void cancelLoadingDialog(){
        if(loadingDialog != null){
            loadingDialog.cancel();
        }
    }

    @Override
    public void onFailure(int code, String message) {
        cancelLoadingDialog();
        MessageReporter.showMessage(AuthActivity.this,
                "Ошибка",
                "Неизвестная ошибка");
    }
}
