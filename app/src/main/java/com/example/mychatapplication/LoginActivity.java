package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.mychatapplication.databinding.ActivityLoginBinding;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.model.sendWS.InitClient;
import com.example.mychatapplication.model.sendWS.LogInServer;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.example.mychatapplication.util.OkHttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String logMeg;
    private JSONObject logResJson;
    private SPRepository spRepository;
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        spRepository = SPRepository.getInstance();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        Rect rect = new Rect();
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int height = rect.height();
                if (screenHeight != height) {
                    spRepository.setSoftKeyboardHeight(screenHeight - height);
                }
            }
        };

        binding.logETAccount.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        binding.logETPassword.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        binding.btLogin.setOnClickListener(this);
        binding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_login) {
            FormBody body = new FormBody.Builder().add("account", binding.logETAccount.getText().toString()).add("password", binding.logETPassword.getText().toString()).build();
            OkHttpUtil.getInstance().sendOkHttpPostRequest(MainApplication.logUrl,body, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    logMeg = e.toString();
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, logMeg, Toast.LENGTH_LONG).show());
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        logMeg = response.body().string();
                        logResJson = new JSONObject(logMeg);
                        //equal和==：内容比较和引用比较
                        if (logResJson.get("status").toString().equals("200") && logResJson.get("meg").toString().equals("登录成功")) {
                            SPRepository.getInstance().saveLoginMsg(true, logResJson.get("jyId").toString());
                            MainApplication.getInstance().user = new User();
                            MainApplication.getInstance().user.setJyId(logResJson.get("jyId").toString());
                            loginViewModel = new ViewModelProvider(LoginActivity.this).get(LoginViewModel.class);
                            loginViewModel.initUserAndNavigate(MainApplication.getInstance().user.getJyId(), new LoginViewModel.NavigateCallback() {
                                @Override
                                public void onReadyToNavigate(User user) {
                                    if(user != null){ MainApplication.getInstance().user = user;}
                                    String initClient = new Gson().toJson(new InitClient(MainApplication.getInstance().user.getJyId(), true));
                                    WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(new LogInServer()));
                                    WebSocketService.getInstance().sendWSStringMsg(initClient);
                                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(Exception e) {
                                    runOnUiThread(() -> {
                                        Toast.makeText(LoginActivity.this, "数据库错误"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("aaaaaaaaaaaaa", e.getMessage() + " ");
                                    });
                                }
                            });
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}