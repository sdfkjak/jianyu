package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.example.mychatapplication.util.ImageUtil;

import java.util.HashMap;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{
    public static String preferenceName = "com.example.myapp.PREFERENCE_LOGIN_STATE";
    private Button bt_login, bt_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPRepository.getInstance().getLoginMsg();
        SPRepository.getInstance().getLoginMsgLiveData().observe(this, new Observer<HashMap<String, String>>() {
            @Override
            public void onChanged(HashMap<String, String> stringStringHashMap) {
                if(stringStringHashMap.get("isLogin").equals("true")){
                    MainApplication.getInstance().user = new MainApplication.User(stringStringHashMap.get("jyid"));
                    WebSocketClass.getInstance();
                    Intent intent = new Intent(LaunchActivity.this, NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    setContentView(R.layout.activity_launch);
                    getWindow().setStatusBarColor(getResources().getColor(R.color.black, null));
                    initWidget();
                    bt_login.setOnClickListener(LaunchActivity.this);
                    bt_reg.setOnClickListener(LaunchActivity.this);
                }
            }
        });
    }

    private void initWidget(){
        bt_login = findViewById(R.id.bt_login);
        bt_reg = findViewById(R.id.bt_register);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                break;
            case R.id.bt_register:
                startActivity(new Intent(LaunchActivity.this, RegisteredActivity.class));
                break;
        }
    }
}