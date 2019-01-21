package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.RadioButton;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.application.App;
import com.sap.uncolor.quiz.main_activity.MainActivity;
import com.sap.uncolor.quiz.models.AuthResponse;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.utils.MessageReporter;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener{

    @BindView(R.id.editTextLogin)
    EditText editTextLogin;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.editTextRepeatPassword)
    EditText editTextRepeatPassword;

    @BindView(R.id.radioButtonMale)
    RadioButton radioButtonMale;

    @BindView(R.id.radioButtonFemale)
    RadioButton radioButtonFemale;

    private AlertDialog loadingDialog;


    public static Intent getInstance(Context context){
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        loadingDialog = LoadingDialog.newInstanceWithoutCancelable(this, LoadingDialog.LABEL_LOADING);
    }

    @OnClick(R.id.buttonRegister)
    void onButtonRegisterClick(){
         String login = editTextLogin.getText().toString();
         String password = editTextPassword.getText().toString();
         String repeatPassword = editTextRepeatPassword.getText().toString();

         if(login.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
             MessageReporter.showMessage(this, "Ошибка", "Заполните все поля");
             return;
         }

         if(!Objects.equals(password, repeatPassword)){
             MessageReporter.showMessage(this, "Ошибка", "Пароли не совпадают");
             return;
         }

         if(!radioButtonMale.isChecked() && !radioButtonFemale.isChecked()){
             MessageReporter.showMessage(this, "Ошибка", "Выберите пол");
             return;
         }

         String sex = "";
         if(radioButtonMale.isChecked()){
             sex = "м";
         }
         else if (radioButtonFemale.isChecked()){
             sex = "ж";
         }
        Api.getSource().register(login, password, sex)
                .enqueue(ApiResponse.getCallback(getApiResponseListener(),this));

    }

    private ApiResponse.ApiResponseListener<AuthResponse> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<AuthResponse>() {
            @Override
            public void onResponse(AuthResponse result) throws IOException {
                cancelLoadingDialog();
                if(result == null){
                    MessageReporter.showMessage(RegisterActivity.this,
                            "Ошибка",
                            "Ошибка при регистрации");
                    return;
                }

                if(result.getResponse().getError() != null){
                    App.Log("error");
                    MessageReporter.showMessage(RegisterActivity.this,
                            "Ошибка",
                            "Ошибка при регистрации");
                    return;
                }
                User user = result.getResponse();
                App.putUserData(user);
                startActivity(MainActivity.getInstance(RegisterActivity.this));
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
        MessageReporter.showMessage(RegisterActivity.this,
                "Ошибка",
                "Неизвестная ошибка");
    }
}
