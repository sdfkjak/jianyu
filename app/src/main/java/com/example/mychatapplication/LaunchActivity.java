package com.example.mychatapplication;

import androidx.core.splashscreen.SplashScreen;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.example.mychatapplication.databinding.ActivityLaunchBinding;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.model.sendWS.InitClient;
import com.example.mychatapplication.model.sendWS.LogInServer;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPDao;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

public class LaunchActivity extends BaseActivity{
    Observer<HashMap<String, String>> observe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        HashMap<String, String> result = SPDao.getInstance().getLoginMsg();
        if(Objects.equals(result.get("isLogin"), "true") && !Objects.equals(result.get("jyid"), "")){
            MainApplication.getInstance().user = new User();
            MainApplication.getInstance().user.setJyId(result.get("jyid"));
            LaunchViewModel launchViewModel = new ViewModelProvider(this).get(LaunchViewModel.class);
            launchViewModel.setUserAndNavigate(result.get("jyid"), new LaunchViewModel.Callback() {
                @Override
                public void onSucceed(User user) {
                    if(user != null) {MainApplication.getInstance().user = user;}
                    String initClient = new Gson().toJson(new InitClient(MainApplication.getInstance().user.getJyId(), true));
                    WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(new LogInServer()));
                    WebSocketService.getInstance().sendWSStringMsg(initClient);
                    Intent intent = new Intent(LaunchActivity.this, NavigationActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(LaunchActivity.this, "数据库错误"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }else{
            ActivityLaunchBinding binding = ActivityLaunchBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            getWindow().setStatusBarColor(getResources().getColor(R.color.black, null));
            binding.btLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
            binding.btRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisteredActivity.class)));
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SPRepository.getInstance().getLoginMsgLiveData().removeObserver(observe);
    }
}