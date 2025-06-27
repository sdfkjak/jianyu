package com.example.mychatapplication;

import static android.text.format.DateUtils.FORMAT_SHOW_TIME;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.util.Log;

//import com.example.mychatapplication.database.UserDBHelper;

import androidx.lifecycle.Observer;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.example.mychatapplication.network.MessageHub;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.SDcardRepository.SDCardRepository;
import com.example.mychatapplication.thread.ServerTime;
import com.google.gson.Gson;

import java.io.File;

public class MainApplication extends Application {
    private final static String tag = "MainApplication";
    private static MainApplication mApp;
    private static final String ip_address = "192.168.1.14";
    public static final String regUrl = "http://" + ip_address + ":8081/register";
    public static final String logUrl = "http://" + ip_address + ":8081/login";
    public static String wbUrl = "ws://" + ip_address + ":8080";
    public static String USERDATABASE = "UserDataBase";
    public static String USERINFODATABASE = "UserInfoDataBase";
    public boolean isStartTime = false;

    public User user;

    public File publicFile, avatarFolder, cacheFolder, chatFolder;
    private SDCardRepository sdCardRepository;
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

    public void clearUser() {
        mApp.user = null;
    }
}

