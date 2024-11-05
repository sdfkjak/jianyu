package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mychatapplication.database.UserInfoDatabase;
import com.example.mychatapplication.network.MessageHub;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.SQLiteRepository.UserDatabase;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
                WebSocketService.getInstance().wsClose(1000, "EXIT");
                MessageHub.getInstance().receiveOnClosing(1000, "EXIT").observe(SettingActivity.this, new Observer<Map<String, String>>() {
                    @Override
                    public void onChanged(Map<String, String> stringStringMap) {
                        WebSocketService.getInstance().clearWebSocketService();
                        MainApplication.getInstance().clearUser();
                        UserDatabase.getInstance().clearUserDatabase();
                        SPRepository.getInstance().saveLoginMsg(false, "");
                        Intent intent = new Intent(SettingActivity.this, LaunchActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                });
            }
        });
//        tv_exit = findViewById(R.id.tv_exit);
//        tv_exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WebSocketClass.getInstance().getWebSocket().close(1000, "EXIT");
//                WebSocketClass.getInstance().registerListener(new WebSocketClass.MyWebSocketListener() {
//                    @Override
//                    public void onMessageReceived(JSONObject jsonObject) throws JSONException {
//
//                    }
//
//                    @Override
//                    public void onMessageReceived(String typeString, String sourceString, String timestampString, byte[] imgBytes) {
//
//                    }
//
//                    @Override
//                    public void onMessageReceived(int code, String reason) {
//                        if(code == 1000 && reason.equals("EXIT")){
//                            WebSocketClass.getInstance().closeWebSocket();
//                            MainApplication.getInstance().clearUser();
//                            UserInfoDatabase.getDatabase(SettingActivity.this).clearUserInfoDatabase();
//                            SPRepository.getInstance().saveLoginMsg(false, "");
//                            SPRepository.getInstance().getIsSaveLoginMsgOver().observe(SettingActivity.this, new Observer<Boolean>() {
//                                @Override
//                                public void onChanged(Boolean aBoolean) {
//                                    if(!aBoolean){
//                                        Intent intent = new Intent(SettingActivity.this, LaunchActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        });
    }
}