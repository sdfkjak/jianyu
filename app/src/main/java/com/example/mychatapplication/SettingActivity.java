package com.example.mychatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mychatapplication.network.MessageHub;
import com.example.mychatapplication.network.NetWorkViewModel;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.SQLiteRepository.UserDatabase;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPDao;

public class SettingActivity extends BaseActivity {
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
                WebSocketService.getInstance().clearWebSocketService();
                UserDatabase.getInstance().clearUserDatabase();
                MessageHub.getInstance().clear();
                NetWorkViewModel.getInstance().clear();
                MainApplication.getInstance().clearUser();

                SPDao.getInstance().synSaveLoginMsg(false, "");
                ActivityCollector.clearAllActivities();
                Intent intent = new Intent(SettingActivity.this, LaunchActivity.class);
                startActivity(intent);
            }
        });
    }
}