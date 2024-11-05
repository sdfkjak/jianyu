package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import com.example.mychatapplication.model.sendWS.SendFriendRequests;
import com.example.mychatapplication.model.FriendRequest;
import com.example.mychatapplication.network.WebSocketService;
import com.google.gson.Gson;

public class ApplyAddFriendActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_applyMeg, et_remark;
    private Button bt_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_add_friend);

        et_applyMeg = findViewById(R.id.et_applyMeg);
        et_remark = findViewById(R.id.et_remark);
        bt_send = findViewById(R.id.bt_send);
        bt_send.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.bt_send){
            String applyMeg = et_applyMeg.getText().toString();
            String remark = et_remark.getText().toString();
            String target = getIntent().getExtras().getString("target");
            SendFriendRequests sendFriendRequests;
            if(applyMeg.isEmpty()){
                sendFriendRequests = new SendFriendRequests(target);
            }else{
                sendFriendRequests = new SendFriendRequests(target, applyMeg);
            }
            WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(sendFriendRequests));
        }
    }
}