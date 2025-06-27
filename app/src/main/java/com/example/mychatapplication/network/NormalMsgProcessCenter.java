package com.example.mychatapplication.network;

import android.util.Log;

import com.example.mychatapplication.thread.ServerTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NormalMsgProcessCenter {
    private static NormalMsgProcessCenter normalMsgProcessCenter;
    private NormalMsgProcessCenter(){}
    public static synchronized NormalMsgProcessCenter getInstance(){
        if(normalMsgProcessCenter == null){
            normalMsgProcessCenter = new NormalMsgProcessCenter();
        }
        return normalMsgProcessCenter;
    }

    public void receiveStringMsg(HashMap<String, String> msg) throws JSONException {
        switch (msg.get("type")){
            case "RESPONSE_TIMESTAMP":
                JSONObject responseTimestampJson  = new JSONObject(msg.get("content"));
                ServerTime.getInstance().setTimestamp(Long.parseLong(responseTimestampJson.getString("timestamp")));
                break;
        }
    }
}
