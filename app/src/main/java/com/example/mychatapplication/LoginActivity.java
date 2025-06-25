package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private String logMeg;
    private JSONObject logResJson;
    private EditText et_account, et_password;
    private ImageView iv_close;
    private Button bt_login;
    private SPRepository spRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        et_account = findViewById(R.id.et_nickname);
        et_account.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        et_password = findViewById(R.id.et_phone);
        et_password.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
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
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, logMeg, Toast.LENGTH_LONG).show());
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
                                Log.d("Login", "登陆成功" + logResJson.get("jyId").toString());
                                SPRepository.getInstance().saveLoginMsg(true, logResJson.get("jyId").toString());
                                try {
                                    MainApplication.getInstance().user = new MainApplication.User(logResJson.get("jyId").toString());
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                WebSocketService.getInstance();
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