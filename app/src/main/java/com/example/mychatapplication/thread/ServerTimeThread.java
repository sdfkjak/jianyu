package com.example.mychatapplication.thread;

import android.util.Log;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.example.mychatapplication.network.WebSocketService;
import com.google.gson.Gson;

public class ServerTimeThread extends Thread{
    private volatile boolean isRunning = true;

    public boolean isRunning() {
        return isRunning;
    }

    public void stopRunning(){
        isRunning = false;
    }
    @Override
    public void run() {
        long startInterval = System.nanoTime();
        while (isRunning) {
            Log.d("服务器时间", "循环中" + MainApplication.getInstance().getIntervalTimestamp() / 1_000_000_000L);
            MainApplication.getInstance().setIntervalTimestamp(System.nanoTime() - startInterval);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.d("服务器时间", "获取时间线程死亡");
                throw new RuntimeException(e);
            }
            if (MainApplication.getInstance().getIntervalTimestamp() >= 60 * 1_000_000_000L && MainApplication.getInstance().user!=null) {
                WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(new TimeStamp()));
                startInterval = System.nanoTime();
                Log.d("服务器时间", "重置时间间隔");
            }
        }
    }
}
