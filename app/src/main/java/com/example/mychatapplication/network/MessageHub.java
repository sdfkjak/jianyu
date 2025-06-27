package com.example.mychatapplication.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.memory.ChatImageCacheManager;
import com.example.mychatapplication.model.ChatImage;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.model.FriendChat;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.model.FriendRequest;
import com.example.mychatapplication.model.receiveWS.ByteMsg;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MessageHub {
    private static final String tag = "MessageHub";
    private static MessageHub messageHub;
    private NetWorkViewModel netWorkViewModel;
    private MutableLiveData<FriendRequest[]> wsFriendApplicationInitMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<FriendRequest> wsFriendApplicationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> wsQueryUser = new MutableLiveData<>();
    private MutableLiveData<String> wsResponseTimestamp = new MutableLiveData<>();
    private MessageHub(){
        netWorkViewModel = NetWorkViewModel.getInstance();
    };
    public synchronized static MessageHub getInstance(){
        if(messageHub == null){
            messageHub = new MessageHub();
        }
        return messageHub;
    }
    public void clear(){
        messageHub = null;
    }
    public LiveData<FriendRequest[]> getWSFriendApplicationInitLiveData() {
        return wsFriendApplicationInitMutableLiveData;
    }
    public LiveData<FriendRequest> getWSFriendApplicationLiveData() {
        return wsFriendApplicationMutableLiveData;
    }

    public LiveData<User> getWSQueryUser(){
        return wsQueryUser;
    }

    public LiveData<String> getWSResponseTimestamp(){ return wsResponseTimestamp; }

    public void receiveStringMsg(HashMap<String, String> msg) throws JSONException {
        switch(msg.get("type")){
            case "USERINIT":
                JSONObject USERINITJson  = new JSONObject(msg.get("content"));
                User user = new Gson().fromJson(USERINITJson.get("userInfo").toString(), User.class);
                netWorkViewModel.insertUser(user);
                break;
            case "FRIENDAPPLICATIONINIT":
                JSONObject FRIENDAPPLICATIONINITJson  = new JSONObject(msg.get("content"));
                JSONArray FRIENDAPPLICATIONINITJsonArray = FRIENDAPPLICATIONINITJson.getJSONArray("FRIENDAPPLICATIONINITLIST");
                FriendRequest[] friendRequests = new Gson().fromJson(FRIENDAPPLICATIONINITJsonArray.toString(), FriendRequest[].class);
                wsFriendApplicationInitMutableLiveData.postValue(friendRequests);
                break;
            case "FRIENDAPPLICATION":
                JSONObject FRIENDAPPLICATIONJson  = new JSONObject(msg.get("content"));
                FriendRequest friendRequest = new Gson().fromJson(FRIENDAPPLICATIONJson.toString(), FriendRequest.class);
                wsFriendApplicationMutableLiveData.postValue(friendRequest);
                break;
            case "QUERYUSERRESULT":
                JSONObject QUERYUSERRESULTJson  = new JSONObject(msg.get("content"));
                User queryUser = new Gson().fromJson(QUERYUSERRESULTJson.get("userInfo").toString(), User.class);
                wsQueryUser.postValue(queryUser);
                break;
            case "ADDFRIENDINFO":
                JSONObject ADDFRIENDINFOJson  = new JSONObject(msg.get("content"));
                User addFriendInfoUser = new Gson().fromJson(ADDFRIENDINFOJson.get("newFriendInfo").toString(), User.class);
                netWorkViewModel.insertUser(addFriendInfoUser);
                break;
            case "FRIENDINIT":
                JSONObject friendInitJson  = new JSONObject(msg.get("content"));
                JsonArray friendInitJsonArray = new Gson().fromJson(friendInitJson.get("FRIENDINIT").toString(), JsonArray.class);
                for (int i = 0; i < friendInitJsonArray.size(); i++) {
                    User friendInitUser = new Gson().fromJson(friendInitJsonArray.get(i), User.class);
                    if(netWorkViewModel.getUserExist(friendInitUser.getJyId())){
                        netWorkViewModel.updateUser(friendInitUser.getJyId(), friendInitUser.getSex(), friendInitUser.getArea(), friendInitUser.getNickname(), friendInitUser.getFriendChatId());
                    }else{
                        netWorkViewModel.insertUser(friendInitUser);
                    }
                }
                break;
            case "RESPONSE_TIMESTAMP":
                JSONObject responseTimestampJson  = new JSONObject(msg.get("content"));
                wsResponseTimestamp.postValue(responseTimestampJson.getString("timestamp"));
                Log.d(tag, "响应的时间戳"+responseTimestampJson.getString("timestamp"));
                break;
            case "FRIENDCHATINIT":
            case "FRIEND_CHAT":
                //不在线时接收的多人消息和在线时的消息都走这
                JSONObject friendChatJson  = new JSONObject(msg.get("content"));
                JSONArray friendChatJSONArray;
                try{
                    friendChatJSONArray = friendChatJson.getJSONArray("FRIENDCHAT");
                }catch (JSONException e){
                    friendChatJSONArray = friendChatJson.getJSONArray("FRIENDCHATINIT");
                }

                for (int i = 0; i < friendChatJSONArray.length(); i++) {
                    String source = friendChatJSONArray.getJSONObject(i).getString("source");
                    if(netWorkViewModel.getUserExist(source)){
                        ChatMessage[] receiveChatMessages = new Gson().fromJson(friendChatJSONArray.getJSONObject(i).getString("chatMessage"), ChatMessage[].class);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String chatMessagesString = netWorkViewModel.getUser(source).getFriendChatMessage();
                                if(chatMessagesString != null){
                                    ArrayList<ChatMessage> chatMessageArrayList = new ArrayList<>(Arrays.asList(new Gson().fromJson(chatMessagesString, ChatMessage[].class)));
                                    for (ChatMessage chatMessage: receiveChatMessages) {
                                        chatMessageArrayList.add(chatMessage);
                                    }
                                    chatMessagesString = new Gson().toJson(chatMessageArrayList);
                                    netWorkViewModel.updateFriendChatMessage(source, chatMessagesString);
                                }else{
                                    chatMessagesString = new Gson().toJson(Arrays.asList(receiveChatMessages));
                                    netWorkViewModel.updateFriendChatMessage(source, chatMessagesString);
                                }

                            }
                        }).start();
                    }
                }

        }

    }
    public void receiveByteMsg(ByteMsg msg){
        Log.d(tag, msg.getType());
        switch (msg.getType()){
            case "USERINIT":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
            case "FRIENDAPPLICATIONINIT":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
            case "FRIENDAPPLICATION":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
            case "QUERYUSERRESULT":
                netWorkViewModel.saveCache(msg.getSource(), msg.getByteMsg());
                break;
            case "ADDFRIENDINFO":
                netWorkViewModel.saveCache(msg.getSource(), msg.getByteMsg());
            case "FRIENDINIT":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
            case "IMAGE":
            case "FRIEND_CHAT":
                ChatImageCacheManager.getInstance().addToCache(new ChatImage(msg.getSource(), Long.parseLong(msg.getTimestamp()), msg.getByteMsg()));
                netWorkViewModel.saveChatImg(msg.getSource(), msg.getTimestamp() + "", msg.getByteMsg());
                break;
//            case "IMAGE":
//
//                break;
        }
    }

//    public LiveData<Map<String, String>> receiveOnClosing(int code, String reason){
//        Map<String, String> map = new HashMap<>();
//        map.put("code", code + "");
//        map.put("reason", reason);
//        return new MutableLiveData<>(map);
//    }
}
