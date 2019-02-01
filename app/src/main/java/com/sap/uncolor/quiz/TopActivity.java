package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.sap.uncolor.quiz.apis.Api;
import com.sap.uncolor.quiz.apis.ApiResponse;
import com.sap.uncolor.quiz.apis.ResponseModel;
import com.sap.uncolor.quiz.models.TopsModel;
import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.models.request_datas.GetTopRequestData;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;
import com.sap.uncolor.quiz.utils.MessageReporter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopActivity extends AppCompatActivity implements ApiResponse.ApiFailureListener{

    @BindView(R.id.recyclerViewTop)
    RecyclerView recyclerViewTop;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private UniversalAdapter adapter;

    public static Intent getInstance(Context context){
        return new Intent(context, TopActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        ButterKnife.bind(this);
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        adapter = new UniversalAdapter();
        adapter.registerRenderer(new TopViewRenderer(User.TYPE, this));
        recyclerViewTop.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress();
        Api.getSource().getTop(new GetTopRequestData())
                .enqueue(ApiResponse.getCallback(getApiResponseListener(), this));
    }

    private ApiResponse.ApiResponseListener<ResponseModel<TopsModel>> getApiResponseListener() {
        return new ApiResponse.ApiResponseListener<ResponseModel<TopsModel>>() {
            @Override
            public void onResponse(ResponseModel<TopsModel> result) {
                hideProgress();
                if (result == null) {
                    MessageReporter.showMessage(TopActivity.this,
                            "Ошибка",
                            "Ошибка получения списка");
                    return;
                }

                int place = result.getResult().getPlace();
                List<User> users = result.getResult().getUsers();
                for (int i = 0; i <users.size() ; i++) {
                    adapter.add(users.get(i));
                }

                if(place > 100){
                    //add individual holder for show user's rating
                }

                else {
                    // select user's holder in top list
                }
            }
        };
    }

    @Override
    public void onFailure(int code, String message) {
        hideProgress();
        MessageReporter.showMessage(TopActivity.this,
                "Ошибка",
                "Ошибка получения списка");
    }

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }


}
