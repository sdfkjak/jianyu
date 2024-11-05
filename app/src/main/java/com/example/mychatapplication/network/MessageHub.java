package com.example.mychatapplication.network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.model.FriendRequest;
import com.example.mychatapplication.model.receiveWS.ByteMsg;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageHub {
    private static final String tag = "MessageHub";
    private static MessageHub messageHub;
    private NetWorkViewModel netWorkViewModel;
    private MutableLiveData<FriendRequest[]> wsFriendApplicationInitMutableLiveData = new MutableLiveData<>();
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

    public LiveData<FriendRequest[]> getWSFriendApplicationInitLiveData() {
        return wsFriendApplicationInitMutableLiveData;
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
                FriendRequest[] friendRequest = new Gson().fromJson(FRIENDAPPLICATIONINITJsonArray.toString(), FriendRequest[].class);
                wsFriendApplicationInitMutableLiveData.postValue(friendRequest);
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
            case "FRIENDINIT":
                JSONObject friendInitJson  = new JSONObject(msg.get("content"));
                JsonArray friendInitJsonArray = new Gson().fromJson(friendInitJson.get("FRIENDINIT").toString(), JsonArray.class);
                for (int i = 0; i < friendInitJsonArray.size(); i++) {
                    User friendInitUser = new Gson().fromJson(friendInitJsonArray.get(i), User.class);
                    if(netWorkViewModel.getUserExist(friendInitUser.getJyId())){
//                        Log.d(tag, netWorkViewModel.getUser(friendInitUser.getJyId()).getFriendChatMessage());
                        Log.d(tag, "1");
                        netWorkViewModel.updateUser(friendInitUser.getJyId(), friendInitUser.getSex(), friendInitUser.getArea(), friendInitUser.getNickname(), friendInitUser.getFriendChatId());
                    }else{
                        Log.d(tag, "2");
                        netWorkViewModel.insertUser(friendInitUser);
                    }
                }
                break;
            case "RESPONSE_TIMESTAMP":
                JSONObject responseTimestampJson  = new JSONObject(msg.get("content"));
                wsResponseTimestamp.postValue(responseTimestampJson.getString("timestamp"));
        }
    }
    public void receiveByteMsg(ByteMsg msg){
        switch (msg.getType()){
            case "USERINIT":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
            case "FRIENDAPPLICATIONINIT":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
            case "QUERYUSERRESULT":
                netWorkViewModel.saveCache(msg.getSource(), msg.getByteMsg());
                break;
            case "FRIENDINIT":
                netWorkViewModel.saveAvatar(msg.getSource(), msg.getByteMsg());
                break;
        }
    }

    public LiveData<Map<String, String>> receiveOnClosing(int code, String reason){
        Map<String, String> map = new HashMap<>();
        map.put("code", code + "");
        map.put("reason", reason);
        return new MutableLiveData<>(map);
    }
}
