package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.example.mychatapplication.adapter.QueryUserDetailAdapter;
import com.example.mychatapplication.commomclass.friendapplication.CommonUserInfo;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QueryUserDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private QueryUserDetailAdapter queryUserDetailAdapter;
    private RecyclerView recyclerView;
    private Button bt_addToPhonebook;
    private Toolbar tb_head;
    private CommonUserInfo commonUserInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_user_detail);

        tb_head = findViewById(R.id.tb_head);
        tb_head.setNavigationIcon(getResources().getDrawable(R.drawable.back, null));
        tb_head.setTitle("");
        setSupportActionBar(tb_head);
        tb_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.rv_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonUserInfo = (CommonUserInfo) getIntent().getExtras().getSerializable("userInfo");
        queryUserDetailAdapter = new QueryUserDetailAdapter(this, commonUserInfo);
        recyclerView.setAdapter(queryUserDetailAdapter);
        bt_addToPhonebook = findViewById(R.id.bt_addtocontact);
        bt_addToPhonebook.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.bt_addtocontact){
            Intent intent = new Intent(this, ApplyAddFriendActivity.class);
            intent.putExtra("target", commonUserInfo.getJyId());
            startActivity(intent);
        }
    }

}