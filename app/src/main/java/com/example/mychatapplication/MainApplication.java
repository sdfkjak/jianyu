package com.example.mychatapplication;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

//import com.example.mychatapplication.database.UserDBHelper;

import com.bumptech.glide.load.engine.Resource;

import okhttp3.WebSocket;

public class MainApplication extends Application {
    private static MainApplication mApp;
    public static final String regUrl = "http://172.19.50.90:8081/register";
    public static final String logUrl = "http://172.19.50.90:8081/login";
    public static String wbUrl = "ws://172.19.50.90:8080";
    public static String USERINFODATABASE = "UserInfoDataBase";
    public User user;
    public static Resources resources;
    public static Context applicationContext;
    public static MainApplication getInstance() {
        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        mApp = this;
        applicationContext = getApplicationContext();
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

