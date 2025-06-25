package com.example.mychatapplication;

import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPDao;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;

import java.util.HashMap;

public class LaunchActivity extends BaseActivity implements View.OnClickListener{
    private static final String tag = "LaunchActivity";
    private Button bt_login, bt_reg;
    Observer<HashMap<String, String>> observe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        HashMap<String, String> result = SPDao.getInstance().getLoginMsg();
        if(result.get("isLogin").equals("true") && !result.get("jyid").equals("")){
            MainApplication.getInstance().user = new MainApplication.User(result.get("jyid"));
            WebSocketService.getInstance();
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
    @Override
    protected void onStop() {
        super.onStop();
        SPRepository.getInstance().getLoginMsgLiveData().removeObserver(observe);
    }
}