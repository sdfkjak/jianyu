package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mychatapplication.database.UserInfoDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {
    private TextView tv_exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_exit = findViewById(R.id.tv_exit);
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketClass.getInstance().getWebSocket().close(1000, "EXIT");
                WebSocketClass.getInstance().registerListener(new WebSocketClass.MyWebSocketListener() {
                    @Override
                    public void onMessageReceived(JSONObject jsonObject) throws JSONException {

                    }

                    @Override
                    public void onMessageReceived(String typeString, String sourceString, String timestampString, byte[] imgBytes) {

                    }

                    @Override
                    public void onMessageReceived(int code, String reason) {
                        if(code == 1000 && reason.equals("EXIT")){
                            WebSocketClass.getInstance().closeWebSocket();
                            MainApplication.getInstance().clearUser();
                            UserInfoDatabase.getDatabase(SettingActivity.this).clearUserInfoDatabase();
                            SharedPreferences sp = getSharedPreferences(LaunchActivity.preferenceName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isLogin", false);
                            editor.putString("jyid", "");
                            editor.apply();
                            Intent intent = new Intent(SettingActivity.this, LaunchActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}