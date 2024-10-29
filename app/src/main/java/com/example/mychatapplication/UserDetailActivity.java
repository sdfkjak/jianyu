package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mychatapplication.adapter.UserDetailAdapter;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;

public class UserDetailActivity extends AppCompatActivity {
    private String jyId;
    private UserInfoRepository userInfoRepository;
    private UserDetailAdapter userDetailAdapter;
    private RecyclerView rv_container;
    private UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        jyId = getIntent().getExtras().getString("jyId");

        userDetailAdapter = new UserDetailAdapter();
        rv_container =  findViewById(R.id.rv_container);
        rv_container.setLayoutManager(new LinearLayoutManager(this));
        rv_container.setAdapter(userDetailAdapter);

        userInfoRepository = new UserInfoRepository(this);
        class GetUserInfoThread extends Thread{
            @Override
            public void run() {
                userInfo = userInfoRepository.getCurrentUserInfo(jyId);
                userDetailAdapter.setUserInfo(userInfo);
            }
        }
        new GetUserInfoThread().start();
//        });
    }
}