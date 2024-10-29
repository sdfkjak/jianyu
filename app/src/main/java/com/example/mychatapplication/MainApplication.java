package com.example.mychatapplication;

import android.app.Application;
import android.content.res.Resources;

//import com.example.mychatapplication.database.UserDBHelper;

import com.bumptech.glide.load.engine.Resource;

import okhttp3.WebSocket;

public class MainApplication extends Application {
    private static MainApplication mApp;
    //websocket连接地址
    //ws://192.168.43.184:8080
    public static String wbUrl = "ws://172.19.50.90:8080";
    public static String USERINFODATABASE = "UserInfoDataBase";
    public User user;
    public static Resources resources;
    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        mApp = this;
    }

    public static class User {
        public String jyId;
        public User(String userId) {
            this.jyId = userId;
        }
    }
    public void clearUser(){
        mApp.user = null;
    }
}

