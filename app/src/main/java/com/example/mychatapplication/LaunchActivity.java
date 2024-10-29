package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.util.ImageUtil;

public class LaunchActivity extends AppCompatActivity implements View.OnClickListener{
    public static String preferenceName = "com.example.myapp.PREFERENCE_LOGIN_STATE";
    private Button bt_login, bt_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        if(sharedPref.getBoolean("isLogin", false)){
            String userJyId = sharedPref.getString("jyid", null);
            MainApplication.getInstance().user = new MainApplication.User(userJyId);
            WebSocketClass.getInstance();
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            setContentView(R.layout.activity_launch);
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, null));
            initWidget();
            bt_login.setOnClickListener(this);
            bt_reg.setOnClickListener(this);
        }
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