package com.example.mychatapplication.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.commomclass.PrivateChat.FriendChatMessage;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okio.ByteString;

public class ChatUtil {
    public static void addFriendChatMessages(String jyid, JSONArray beAddFriendChatMessageJsonArray) {
        class GetUserInfo implements Runnable {
            UserInfoRepository userInfoRepository = new UserInfoRepository(MainApplication.getInstance().getApplicationContext());
            @Override
            public void run() {
                UserInfo userInfo = userInfoRepository.getCurrentUserInfo(jyid);
                try {
                    String userFriendChatMessageString = userInfo.getFriendChatMessages();
                    if (userFriendChatMessageString == null) {
                        userInfo.setFriendChatMessages(beAddFriendChatMessageJsonArray.toString());
                        userInfoRepository.updateUserInfo(userInfo);
                    } else if (new JSONArray(userFriendChatMessageString).length() == 0) {
                        userInfo.setFriendChatMessages(beAddFriendChatMessageJsonArray.toString());
                        userInfoRepository.updateUserInfo(userInfo);
                    } else {
                        JSONArray userFriendChatMessageJsonArray = new JSONArray(userFriendChatMessageString);
                        if (userFriendChatMessageJsonArray.length() != 0) {
                            long minTimestamp = userFriendChatMessageJsonArray.getJSONObject(userFriendChatMessageJsonArray.length() - 1).getLong("timestamp");
                            for (int i = 0; i < beAddFriendChatMessageJsonArray.length(); i++) {
                                if (beAddFriendChatMessageJsonArray.getJSONObject(i).getLong("timestamp") > minTimestamp) {
                                    for (int k = i; k < beAddFriendChatMessageJsonArray.length(); k++) {
                                        userFriendChatMessageJsonArray.put(beAddFriendChatMessageJsonArray.getJSONObject(k));
                                    }
                                    break;
                                }
                            }
                            userInfo.setFriendChatMessages(userFriendChatMessageJsonArray.toString());
                            userInfoRepository.updateUserInfo(userInfo);
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        new Thread(new GetUserInfo()).start();
    }
}
