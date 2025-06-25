package com.example.mychatapplication;

import static android.text.format.DateUtils.FORMAT_SHOW_TIME;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.util.Log;

//import com.example.mychatapplication.database.UserDBHelper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.load.engine.Resource;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.example.mychatapplication.network.MessageHub;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.SDcardRepository.SDCardRepository;
import com.example.mychatapplication.thread.ServerTimeThread;
import com.google.gson.Gson;

import java.io.File;

import okhttp3.WebSocket;

public class MainApplication extends Application {
    private final static String tag = "MainApplication";
    private static MainApplication mApp;
    private static final String ip_address = "192.168.1.14";
    public static final String regUrl = "http://" + ip_address + ":8081/register";
    public static final String logUrl = "http://" + ip_address + ":8081/login";
    public static String wbUrl = "ws://" + ip_address + ":8080";
    public static String USERDATABASE = "UserDataBase";
    public static String USERINFODATABASE = "UserInfoDataBase";

    private long serverTimestamp;
    private long intervalTimestamp;
    public boolean isStartTime = false;
    private MessageHub messageHub;

    public User user;

    public File publicFile, avatarFolder, cacheFolder, chatFolder;
    private SDCardRepository sdCardRepository;
    public static Resources resources;
    public static Context applicationContext;
    public static ServerTimeThread serverTimeThread = new ServerTimeThread();

    public static MainApplication getInstance() {
        return mApp;
    }

    public long getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(long serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }

    public long getIntervalTimestamp() {
        return intervalTimestamp;
    }

    public void setIntervalTimestamp(long intervalTimestamp) {
        this.intervalTimestamp = intervalTimestamp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        mApp = this;
        applicationContext = getApplicationContext();
        sdCardRepository = SDCardRepository.getInstance();
        publicFile = applicationContext.getExternalFilesDir(null);
        avatarFolder = new File(applicationContext.getExternalFilesDir(null), "avatar");
        cacheFolder = new File(applicationContext.getExternalFilesDir(null), "cache");
        chatFolder = new File(applicationContext.getExternalFilesDir(null), "chat");
        if(!avatarFolder.exists()){
            sdCardRepository.createFolder(publicFile, "avatar");
        }
        if(!cacheFolder.exists()){
            sdCardRepository.createFolder(publicFile, "cache");
        }
        if(!chatFolder.exists()){
            sdCardRepository.createFolder(publicFile, "chat");
        }

        Log.d(tag, avatarFolder.getAbsolutePath());
        Log.d(tag, cacheFolder.getAbsolutePath());

    }

    public void startTime(){
        WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(new TimeStamp()));
        messageHub = MessageHub.getInstance();
        messageHub.getWSResponseTimestamp().observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                setServerTimestamp(Long.parseLong(s));
                Log.d("服务器时间", DateUtils.formatDateTime(MainApplication.applicationContext, serverTimestamp, FORMAT_SHOW_TIME) + "获取的服务器时间");
                intervalTimestamp = 0;
                if(!isStartTime){
                    serverTimeThread.start();
                    isStartTime = true;
                }
            }
        });
    }
    public long getTimeStamp() {
        Log.d(tag, DateUtils.formatDateTime(this, serverTimestamp, FORMAT_SHOW_TIME) + "服务器时间");
        Log.d(tag, intervalTimestamp / 1_000_000_000L + "距离上次获取服务器时间过去秒数");
        Log.d(tag, DateUtils.formatDateTime(this, serverTimestamp + intervalTimestamp / 1_000_000_000L, FORMAT_SHOW_TIME) + "具体时间");
        return serverTimestamp + intervalTimestamp / 1_000_000_000L;
    }

    public static class User {
        public String jyId;

        public User(String userId) {
            this.jyId = userId;
        }
    }

    public void clearUser() {
        mApp.user = null;
    }
}

