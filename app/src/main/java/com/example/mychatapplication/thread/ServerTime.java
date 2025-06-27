package com.example.mychatapplication.thread;

import android.util.Log;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.example.mychatapplication.network.WebSocketService;
import com.google.gson.Gson;

public class ServerTime{
    private static ServerTime serverTime;
    private ServerTime(){}
    public static synchronized ServerTime getInstance(){
        if(serverTime == null){
            serverTime = new ServerTime();
        }
        return serverTime;
    }
    private long timestamp;
    private long detailTimestamp;
    private long lastSetTimestamp;

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        lastSetTimestamp = System.currentTimeMillis();
    }

    public long getDetailTimestamp() {
        Log.d("ServerTimexxx", "timestamp:" + timestamp + "System.currentTimeMillis():" + System.currentTimeMillis() +"intervalTimestamp:" + (System.currentTimeMillis() - lastSetTimestamp));
        return timestamp + System.currentTimeMillis() - lastSetTimestamp;
    }
}
