package com.sap.uncolor.quiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sap.uncolor.quiz.models.User;
import com.sap.uncolor.quiz.universal_adapter.UniversalAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TopActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewTop)
    RecyclerView recyclerViewTop;

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


}
