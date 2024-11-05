package com.example.mychatapplication;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.mychatapplication.commomclass.friendapplication.CommonUserInfo;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.model.FriendRequest;
import com.example.mychatapplication.util.FileUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClass {
    private List<MyWebSocketListener> myWebSocketListenerList = new ArrayList<>();
    private static WebSocketClass webSocketclass;
    private OkHttpClient client;
    private Request request;
    private okhttp3.WebSocket webSocket;
    private MutableLiveData<ArrayList<FriendRequest>> listMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private static UserInfoRepository userInfoRepository = new UserInfoRepository(MainApplication.getInstance().getApplicationContext());
    private static LiveData<List<UserInfo> > newFriendLiveData;
    private static HashMap<String, String> newFriendMessageMap = new HashMap<>();
    private static MutableLiveData<LinkedList<UserInfoRepository.InsertUpdate>> insertUpdateLinkedListMutableLiveData = new MutableLiveData<>(new LinkedList<>());

    public MutableLiveData<ArrayList<FriendRequest>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public static MutableLiveData<LinkedList<UserInfoRepository.InsertUpdate>> getInsertUpdateLinkedListMutableLiveData() {
        return insertUpdateLinkedListMutableLiveData;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public static synchronized WebSocketClass getInstance() {
        if (webSocketclass == null) {
            webSocketclass = new WebSocketClass();
            userInfoRepository = new UserInfoRepository(MainApplication.getInstance().getApplicationContext());
            insertUpdateLinkedListMutableLiveData.observeForever(new Observer<LinkedList<UserInfoRepository.InsertUpdate>>() {
                @Override
                public void onChanged(LinkedList<UserInfoRepository.InsertUpdate> insertUpdates) {
                    Log.d("插入更新", "触发改变");
                    if (insertUpdates.size() != 0) {
                        Log.d("插入更新", "执行insertUpdate()");
                        String executeCode = insertUpdates.getFirst().insertUpdate();
                        Log.d("插入更新", "返回结果" + executeCode);
                    }
                }
            });
            newFriendLiveData = userInfoRepository.getAllUserInfoLiveData();
            newFriendLiveData.observeForever(new Observer<List<UserInfo>>() {
                @Override
                public void onChanged(List<UserInfo> userInfoList) {
                    for (UserInfo userInfo: userInfoList) {
                        for (String jyid: newFriendMessageMap.keySet()) {
                            Log.d("FRIENDCHAT", jyid);
                            Log.d("FRIENDCHAT", userInfo.getJyId());
                            if(userInfo.getJyId().equals(jyid)){
                                userInfoRepository.updateItem(jyid, "friendChatMessages", newFriendMessageMap.get(jyid));
                                newFriendMessageMap.remove(jyid);
                            }
                        }
                    }
                }
            });
        }
        return webSocketclass;
    }

    private WebSocketClass() {
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        this.request = new Request.Builder()
                .url(MainApplication.wbUrl)
                .build();
        WebSocketListener listener = createWebSocketListener();
        webSocket = client.newWebSocket(request, listener);

    }
    private WebSocketListener createWebSocketListener(){
        return new WebSocketListener() {
            @Override
            public void onOpen(okhttp3.WebSocket webSocket, Response response) {
                JSONObject initJson = new JSONObject();
                try {
                    initJson.put("type", "INITCLIENT");
                    initJson.put("jyId", MainApplication.getInstance().user.jyId);
                    if(userInfoRepository.userInfoExist(MainApplication.getInstance().user.jyId)){
                        initJson.put("needInfo", false);
                    }else{
                        initJson.put("needInfo", true);
                    }

                    webSocket.send(initJson.toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onMessage(okhttp3.WebSocket webSocket, String text) {
                Log.d("收到消息", text);
                Gson gson = new Gson();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(text);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    notifyListeners(jsonObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                try {
                    if (jsonObject.getString("type").equals("USERINIT")) {
                        JSONObject userJsonObject = jsonObject.getJSONObject("userInfo");
                        UserInfo userInfo = gson.fromJson(userJsonObject.toString(), UserInfo.class);
                        insertUpdateLinkedListMutableLiveData.getValue().addLast(userInfoRepository.insertUpdate(userInfo));
                        insertUpdateLinkedListMutableLiveData.postValue(insertUpdateLinkedListMutableLiveData.getValue());
                    } else {
                        ;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (jsonObject.getString("type").equals("FRIENDAPPLICATION")) {
//                        boolean insertBefore = false;
//                        FriendRequest friendRequest = gson.fromJson(jsonObject.toString(), FriendRequest.class);
//                        for (int i = 0; i < listMutableLiveData.getValue().size(); i++) {
//                            if(listMutableLiveData.getValue().get(i).getCommonUserInfo().getJyId().equals(friendRequest.getCommonUserInfo().getJyId())){
//                                insertBefore = true;
//                                friendRequest.getCommonUserInfo().setAvatarBytes(listMutableLiveData.getValue().get(i).getCommonUserInfo().getAvatarBytes());
//                                listMutableLiveData.getValue().remove(i);
//                                listMutableLiveData.getValue().add(friendRequest);
//                            }
//                        }
//                        if(!insertBefore){
//                            listMutableLiveData.getValue().add(friendRequest);
//                        }
                    } else {
                        ;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (jsonObject.getString("type").equals("FRIENDAPPLICATIONINIT")) {
//                        Log.d("好友申请", "JSON数据");
//                        boolean insertBefore = false;
//                        JSONArray friendApplicationListJson = jsonObject.getJSONArray("FRIENDAPPLICATIONINITLIST");
//                        FriendRequest[] receiveFriendRequest = gson.fromJson(friendApplicationListJson.toString(), FriendRequest[].class);
//                        for (FriendRequest friendRequest : receiveFriendRequest) {
//                            for (int i = 0; i < listMutableLiveData.getValue().size(); i++) {
//                                if(listMutableLiveData.getValue().get(i).getCommonUserInfo().getJyId().equals(friendRequest.getCommonUserInfo().getJyId())){
//                                    insertBefore = true;
//                                    Log.d("好友申请", "已经插入图片");
//                                    friendRequest.getCommonUserInfo().setAvatarBytes(listMutableLiveData.getValue().get(i).getCommonUserInfo().getAvatarBytes());
//                                    listMutableLiveData.getValue().remove(i);
//                                    listMutableLiveData.getValue().add(friendRequest);
//                                    listMutableLiveData.postValue(listMutableLiveData.getValue());
//                                    Log.d("好友申请", String.valueOf(listMutableLiveData.getValue().size()));
//                                }
//                            }
//                            if(!insertBefore){
//                                Log.d("好友申请", "还没插入图片");
//                                listMutableLiveData.getValue().add(friendRequest);
//                            }
//                        }
                    } else {
                        ;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (jsonObject.getString("type").equals("ADDFRIENDINFO") || jsonObject.getString("type").equals("FRIENDINIT")) {
                        JSONArray receiveUserInfoListJson;
                        try {
                            receiveUserInfoListJson = jsonObject.getJSONArray("newFriendInfo");
                        } catch (JSONException e) {
                            receiveUserInfoListJson = jsonObject.getJSONArray("FRIENDINIT");
                        }
                        UserInfo[] receiveUserInfoList = gson.fromJson(receiveUserInfoListJson.toString(), UserInfo[].class);
                        for (int i = 0; i < receiveUserInfoList.length; i++) {
                            insertUpdateLinkedListMutableLiveData.getValue().addLast(userInfoRepository.insertUpdate(receiveUserInfoList[i]));
                            insertUpdateLinkedListMutableLiveData.postValue(insertUpdateLinkedListMutableLiveData.getValue());
                        }
                        Log.d("FRIENDCHAT", "插入更新完成");
                    } else {
                        ;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (jsonObject.getString("type").equals("FRIENDCHAT") || jsonObject.getString("type").equals("FRIENDCHATINIT") || jsonObject.getString("type").equals("FRIENDCHATINIT")) {
                        JSONArray receiveFriendChatMessageListJson;
                        try {
                            receiveFriendChatMessageListJson = jsonObject.getJSONArray("FRIENDCHAT");
                        } catch (JSONException e) {
                            receiveFriendChatMessageListJson = jsonObject.getJSONArray("FRIENDCHATINIT");
                        }
                        for (int j = 0; j < receiveFriendChatMessageListJson.length(); j++) {
                            UserInfo userInfo = userInfoRepository.getCurrentUserInfo(receiveFriendChatMessageListJson.getJSONObject(j).getString("chatTarget"));
                            JSONArray messageJsonArray = receiveFriendChatMessageListJson.getJSONObject(j).getJSONArray("friendChatMessage");
                            if(userInfo == null){
                                newFriendMessageMap.put(receiveFriendChatMessageListJson.getJSONObject(j).getString("chatTarget"), messageJsonArray.toString());
                            }else{
                                String userFriendChatMessageString = userInfo.getFriendChatMessages();
                                if (userFriendChatMessageString == null) {
                                    userInfo.setFriendChatMessages(messageJsonArray.toString());
                                } else if (new JSONArray(userFriendChatMessageString).length() == 0) {
                                    userInfo.setFriendChatMessages(messageJsonArray.toString());
                                } else {
                                    JSONArray userFriendChatMessageJsonArray = new JSONArray(userFriendChatMessageString);
                                    long minTimestamp = userFriendChatMessageJsonArray.getJSONObject(userFriendChatMessageJsonArray.length() - 1).getLong("timestamp");
                                    Log.d("aaaaaaaaaaaaa", userFriendChatMessageJsonArray.getJSONObject(userFriendChatMessageJsonArray.length() - 1).getString("content"));
                                    Log.d("aaaaaaaaaaaaa", String.valueOf(minTimestamp));
                                    for (int i = 0; i < messageJsonArray.length(); i++) {
                                        if (messageJsonArray.getJSONObject(i).getLong("timestamp") > minTimestamp) {
                                            Log.d("aaaaaaaaaaaaa", messageJsonArray.getJSONObject(i).getString("content"));
                                            Log.d("aaaaaaaaaaaaa", messageJsonArray.getJSONObject(i).getString("timestamp"));
                                            for (int k = i; k < messageJsonArray.length(); k++) {
                                                userFriendChatMessageJsonArray.put(messageJsonArray.getJSONObject(k));
                                            }
                                            break;
                                        }
                                    }
                                    userInfo.setFriendChatMessages(userFriendChatMessageJsonArray.toString());
                                }
                                userInfoRepository.updateUserInfo(userInfo);
                            }
                        }

                    } else {
                        ;
                    }
                } catch (JSONException e) {
                    Log.d("初始时的FriendChatMessages", "asdfasdfasdf");
                    throw new RuntimeException(e);
                }
                try {
                    if (jsonObject.getString("type").equals("MODIFYPERSONALINFO")) {
                        JSONObject receiveUpDateUserInfoJson = jsonObject.getJSONObject("user_info");
                        UserInfo receiveUserInfoList = gson.fromJson(receiveUpDateUserInfoJson.toString(), UserInfo.class);
                        userInfoRepository.updateUserInfo(receiveUserInfoList);
                    } else {
                        ;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                super.onMessage(webSocket, bytes);
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
                String img = Base64.encodeToString(imgBytes, 0);
                Log.d("字节数据类型", typeString);
                Log.d("字节数据来源", sourceString);
                Log.d("字节数据时间戳", timestampString);
                Log.d("字节数据图片", String.valueOf(img.length()));

                notifyListeners(typeString, sourceString, timestampString, imgBytes);

                if (typeString.equals("USERINIT")) {
                    insertUpdateLinkedListMutableLiveData.getValue().addLast(userInfoRepository.insertUpdate(new UserInfo(sourceString, img)));
                    insertUpdateLinkedListMutableLiveData.postValue(insertUpdateLinkedListMutableLiveData.getValue());
                }
                if (typeString.equals("FRIENDAPPLICATIONINIT")) {
//                    Log.d("好友申请", "字节数据");
//                    boolean insertBefore = false;
//                    ArrayList<FriendRequest> friendRequestArrayList = listMutableLiveData.getValue();
//                    for (int i = 0; i < friendRequestArrayList.size(); i++) {
//                        if(friendRequestArrayList.get(i).getCommonUserInfo().getJyId().equals(sourceString)){
//                            insertBefore = true;
//                            Log.d("好友申请", "已经插入个人信息");
//                            listMutableLiveData.getValue().get(i).getCommonUserInfo().setAvatarBytes(imgBytes);
//                            listMutableLiveData.postValue(listMutableLiveData.getValue());
//                            break;
//                        }
//                    }
//                    if(!insertBefore){
//                        Log.d("好友申请", "还没插入个人信息");
//                        listMutableLiveData.getValue().add(new FriendRequest(new CommonUserInfo(sourceString, imgBytes)));
//                    }
                }
                if (typeString.equals("ADDFRIENDINFO") || typeString.equals("FRIENDINIT")) {
                    insertUpdateLinkedListMutableLiveData.getValue().addLast(userInfoRepository.insertUpdate(new UserInfo(sourceString, img)));
                    insertUpdateLinkedListMutableLiveData.postValue(insertUpdateLinkedListMutableLiveData.getValue());
                }
                if (typeString.equals("MODIFYPERSONALINFO")) {
                    userInfoRepository.updateItem(sourceString, "avatar", img);
                }
                if (typeString.equals("FRIENDCHATIMAGEMESSAGE")) {
                    String fileName = timestampString + ".jpg";
                    String destinationDirectory = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + sourceString;
                    FileUtil.saveImageFromByteString(fileName, destinationDirectory, imgBytes);
                }

            }

            @Override
            public void onClosing(okhttp3.WebSocket webSocket, int code, String reason) {
                notifyListeners(code, reason);
                super.onClosing(webSocket, code, reason);
                Log.d("WebSocket 连接失败 onClosing：", reason);
            }

            @Override
            public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);

                if (response != null) {
                    Log.d("WebSocket 连接失败 onFailure：", response.message());
                }
                Log.d( "WebSocket 连接失败异常原因：", t.getMessage());
//                WebSocketClass.getInstance().webSocket = client.newWebSocket(request, createWebSocketListener());
            }
        };
    }

    public void closeWebSocket(){
        this.webSocketclass = null;
    }

    public interface MyWebSocketListener{
        void onMessageReceived(JSONObject jsonObject) throws JSONException;
        void onMessageReceived(String typeString, String sourceString, String timestampString, byte[] imgBytes);
        void onMessageReceived(int code, String reason);
    }
    public void registerListener(MyWebSocketListener myWebSocketListener){
        myWebSocketListenerList.add(myWebSocketListener);
    }
    public void unregisterListener(MyWebSocketListener myWebSocketListener){
        myWebSocketListenerList.remove(myWebSocketListener);
    }
    private void notifyListeners(JSONObject jsonObject) throws JSONException {
        for (MyWebSocketListener myWebSocketListener:myWebSocketListenerList) {
            myWebSocketListener.onMessageReceived(jsonObject);
        }
    }
    private void notifyListeners(String typeString, String sourceString, String timestampString, byte[] imgBytes){
        for (MyWebSocketListener myWebSocketListener:myWebSocketListenerList) {
            myWebSocketListener.onMessageReceived(typeString, sourceString, timestampString, imgBytes);
        }
    }
    private void notifyListeners(int code, String reason){
        for (MyWebSocketListener myWebSocketListener:myWebSocketListenerList) {
            myWebSocketListener.onMessageReceived(code, reason);
        }
    }
}
