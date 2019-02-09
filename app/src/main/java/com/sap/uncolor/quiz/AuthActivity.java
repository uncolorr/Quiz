package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.main_activity.MainActivity;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.models.request_datas.SignInRequestData;
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

    public static Intent getInstance(Context context){
        return new Intent(context, AuthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if(App.isAuth()){
            startActivity(MainActivity.getInstance(this));
            finish();
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
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();

        Api.getSource().login(new SignInRequestData(login, password))
                .enqueue(ApiResponse.getCallback(getLoginResponseListener(), this));
    }

    private ApiResponse.ApiResponseListener<ResponseModel<User>> getLoginResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<User>>() {
            @Override
            public void onResponse(ResponseModel<User> result) {
                cancelLoadingDialog();
                if(result == null || result.getResult() == null){
                    MessageReporter.showMessage(AuthActivity.this,
                            "Ошибка",
                            "Ошибка авторизации");
                    return;
                }
                    User user = result.getResult();
                    App.Log("user name: " + user.getLogin());
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
