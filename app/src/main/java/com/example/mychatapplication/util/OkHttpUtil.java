package com.example.mychatapplication.util;

import com.example.mychatapplication.MainApplication;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
    private static OkHttpUtil okHttpUtil;
    private OkHttpUtil(){};
    public synchronized static OkHttpUtil getInstance(){
        if(okHttpUtil == null){
            okHttpUtil = new OkHttpUtil();
        }
        return okHttpUtil;
    }

    public void sendOkHttpPostRequest(String address, RequestBody body, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().post(body).url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
