package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String logMeg;
    private JSONObject logResJson;
    private EditText et_account, et_password;
    private ImageView iv_close;
    private Button bt_login;
    private Intent mIntent;
    private UserInfoRepository userInfoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_account = findViewById(R.id.et_nickname);
        et_password = findViewById(R.id.et_phone);
        bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(this);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_login) {
            FormBody body = new FormBody.Builder().add("account", et_account.getText().toString()).add("password", et_password.getText().toString()).build();
            OkHttpUtil.getInstance().sendOkHttpPostRequest(MainApplication.logUrl,body, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    logMeg = e.toString();
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, logMeg, Toast.LENGTH_LONG));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    logMeg = response.body().string();
                    try {
                        logResJson = new JSONObject(logMeg);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> {
                        try {
                            //equal和==：内容比较和引用比较
                            if (logResJson.get("status").toString().equals("200") && logResJson.get("meg").toString().equals("登录成功")) {
                                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(LaunchActivity.preferenceName, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLogin", true);
                                editor.putString("jyid", logResJson.get("jyId").toString());
                                editor.apply();
                                MainApplication.getInstance().user = new MainApplication.User(logResJson.get("jyId").toString());
                                WebSocketClass.getInstance();
                                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            });
        }
    }
}