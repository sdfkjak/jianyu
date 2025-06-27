package com.example.mychatapplication.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.model.receiveWS.ByteMsg;
import com.example.mychatapplication.model.sendWS.InitClient;
import com.example.mychatapplication.model.sendWS.LogInServer;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketService {
    private static final String tag = "WebSocketService";
    private static WebSocketService webSocketService;
    private OkHttpClient okHttpClient;
    private Request request;
    private WebSocket webSocket;

    private WebSocketService() {
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        request = new Request.Builder()
                .url(MainApplication.wbUrl)
                .build();
        WebSocketListener listener = createWebSocketListener();
        webSocket = okHttpClient.newWebSocket(request, listener);

    }

    public synchronized static WebSocketService getInstance() {
        if (webSocketService == null) {
            webSocketService = new WebSocketService();
        }
        return webSocketService;
    }

    private WebSocketListener createWebSocketListener() {
        return new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                super.onOpen(webSocket, response);
//                if(MainApplication.getInstance().user != null){
//                    WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(new LogInServer()));
//                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                super.onMessage(webSocket, text);
                Log.d(tag, "接收" + text);
                try {
                    HashMap<String, String> result = paresStringMsg(text);
                    DistributionHub.getInstance().distributeStringMessage(result);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
                ByteMsg byteMsg = paresByteMsg(bytes);
                DistributionHub.getInstance().distributeByteMessage(byteMsg);
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d("WebSocket 连接正在关闭 onClosing：", reason);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d("WebSocket 连接已经关闭 onClosing：", reason);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                if (response != null) {
                    Log.d("WebSocket 连接失败 onFailure：", response.message());
                }
                Log.d( "WebSocket 连接失败异常原因：", t.getMessage());
                WebSocketService.getInstance().webSocket = okHttpClient.newWebSocket(request, createWebSocketListener());
                if(MainApplication.getInstance().user != null){
                    WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(new LogInServer()));
                }
            }
        };
    }
    public void sendWSStringMsg(String jsonString) {
        Log.d(tag, "发送" + jsonString);
        webSocket.send(jsonString);
    }
    public void sendWSByteStringMsg(ByteString byteString) {
        Log.d(tag, "发送ByteString");
        webSocket.send(byteString);
    }
    public void wsClose(int code, String reason){
        webSocket.close(code, reason);
    }
    public void clearWebSocketService(){
        webSocketService = null;
    }
    private HashMap<String, String> paresStringMsg(String text) throws JSONException {
        HashMap<String, String> result = new HashMap<>();
        JSONObject jsonObject = new JSONObject(text);
        result.put("type", jsonObject.getString("type"));
        jsonObject.remove("type");
        result.put("content", jsonObject.toString());
        return result;
    }
    private ByteMsg paresByteMsg(ByteString bytes) {
        String labelString = bytes.substring(0, 100).utf8();
        String[] labelStrings = labelString.split("\\|");
        String typeString = "type", sourceString = "source", timestampString = "timestamp";
        for (int i = 0; i < labelStrings.length; i++) {
            switch (i) {
                case 0:
                    typeString = labelStrings[0];
                    break;
                case 1:
                    sourceString = labelStrings[1];
                    break;
                case 2:
                    timestampString = labelStrings[2];
                    break;
                default:
                    ;
            }
        }
        // 将ByteString转换为字节数组
        byte[] imgBytes = bytes.substring(100, bytes.size()).toByteArray();
        Log.d("WebSocketService", "字节数据类型"+typeString);
        Log.d("WebSocketService", "字节数据来源"+sourceString);
        Log.d("WebSocketService", "字节数据时间戳"+ timestampString);
        Log.d("WebSocketService", "字节数据图片长度"+String.valueOf(imgBytes.length));
        return new ByteMsg(typeString, sourceString, timestampString, imgBytes);
    }
}
