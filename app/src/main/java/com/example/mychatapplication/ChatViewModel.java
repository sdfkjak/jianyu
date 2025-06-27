package com.example.mychatapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.ChatDetailItem;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.network.MessageHub;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;
import com.example.mychatapplication.util.TimeUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {
    private UserRepository userRepository;
    private MessageHub messageHub;
    private List<ChatDetailItem> chatDetailItemList = new ArrayList<>();
    private int addChatDetailItemCount = 0;
    private boolean isWaitForSend = false;

    public ChatViewModel() {
        super();
        userRepository = new UserRepository();
        messageHub = MessageHub.getInstance();
    }

    public boolean isWaitForSend() {
        return isWaitForSend;
    }

    public void setWaitForSend(boolean waitForSend) {
        isWaitForSend = waitForSend;
    }

    public List<ChatDetailItem> getChatDetailItemList() {
        return chatDetailItemList;
    }

    public int getAddChatDetailItemCount() {
        return addChatDetailItemCount;
    }

    public void addChatDetailItemList(List<ChatMessage> chatMessageList) {
        if (chatMessageList != null && chatMessageList.size() != 0) {
            if(chatDetailItemList == null || chatDetailItemList.size() == 0){
                chatDetailItemList.add(new ChatDetailItem(ChatDetailItem.ChatType.TIME.toString(), chatMessageList.get(0).getTimestamp()));
                for (int i = 0; i < chatMessageList.size(); i++) {
                    if (chatMessageList.get(i).getType().equals("TEXT")) {
                        chatDetailItemList.add(new ChatDetailItem(chatMessageList.get(i).getSource().equals(MainApplication.getInstance().user.getJyId()) ? ChatDetailItem.ChatType.MYCHAT.toString() : ChatDetailItem.ChatType.OTHERCHAT.toString(), chatMessageList.get(i).getContent(), chatMessageList.get(i).getTimestamp()));
                    } else if (chatMessageList.get(i).getType().equals("IMAGE")) {
                        chatDetailItemList.add(new ChatDetailItem(chatMessageList.get(i).getSource().equals(MainApplication.getInstance().user.getJyId()) ? ChatDetailItem.ChatType.MYCHAT.toString() : ChatDetailItem.ChatType.OTHERCHAT.toString(), chatMessageList.get(i).getTimestamp(), chatMessageList.get(i).getWidth(), chatMessageList.get(i).getHeight()));
                    }
                    if(i + 1 < chatMessageList.size()){
                        if (chatMessageList.get(i + 1).getTimestamp() - chatMessageList.get(i).getTimestamp() > 60 * 1000) {
                            chatDetailItemList.add(new ChatDetailItem(ChatDetailItem.ChatType.TIME.toString(), chatMessageList.get(i + 1).getTimestamp()));
                            Log.d("服务器时间", TimeUtil.detailDateDisplayFormat(MainApplication.applicationContext,chatMessageList.get(i + 1).getTimestamp())+ "添加时间");
                        }
                    }
                }
                addChatDetailItemCount = chatDetailItemList.size();
                return;
            }
            if(chatDetailItemList != null && chatDetailItemList.size() != 0){
                int startCount = chatDetailItemList.size();
                if (chatMessageList.get(0).getTimestamp() - chatDetailItemList.get(chatDetailItemList.size() - 1).getTimestamp() > 60 * 1000) {
                    chatDetailItemList.add(new ChatDetailItem(ChatDetailItem.ChatType.TIME.toString(), chatMessageList.get(0).getTimestamp()));
                }
                for (int i = 0; i < chatMessageList.size(); i++) {
                    if (chatMessageList.get(i).getType().equals("TEXT")) {
                        chatDetailItemList.add(new ChatDetailItem(chatMessageList.get(i).getSource().equals(MainApplication.getInstance().user.getJyId()) ? ChatDetailItem.ChatType.MYCHAT.toString() : ChatDetailItem.ChatType.OTHERCHAT.toString(), chatMessageList.get(i).getContent(), chatMessageList.get(i).getTimestamp()));
                    } else if (chatMessageList.get(i).getType().equals("IMAGE")) {
                        chatDetailItemList.add(new ChatDetailItem(chatMessageList.get(i).getSource().equals(MainApplication.getInstance().user.getJyId()) ? ChatDetailItem.ChatType.MYCHAT.toString() : ChatDetailItem.ChatType.OTHERCHAT.toString(), chatMessageList.get(i).getTimestamp(), chatMessageList.get(i).getWidth(), chatMessageList.get(i).getHeight()));
                    }
                    if(i + 1 < chatMessageList.size()){
                        if (chatMessageList.get(i + 1).getTimestamp() - chatMessageList.get(i).getTimestamp() > 60 * 1000) {
                            chatDetailItemList.add(new ChatDetailItem(ChatDetailItem.ChatType.TIME.toString(), chatMessageList.get(i + 1).getTimestamp()));
                        }
                    }
                }
                addChatDetailItemCount = chatDetailItemList.size() - startCount;
            }
        }
    }

    public LiveData<User> getUserLiveData(String jyId) {
        return userRepository.getUserLiveData(jyId);
    }

    public LiveData<String> getWSResponseTimestamp() {
        return messageHub.getWSResponseTimestamp();
    }

    public void updateFriendChatMessages(String jyId, String friendChatMessage) {
        userRepository.updateFriendChatMessage(jyId, friendChatMessage);
    }

    public User getUser(String jyId) {
        return userRepository.getUser(jyId);
    }

    public void insertSendMsgToDB(String jyId, ChatMessage needInsertMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject needInsertMessageJSONObject = null;
                try {
                    needInsertMessageJSONObject = new JSONObject(new Gson().toJson(needInsertMessage));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                User user = getUser(jyId);
                String friendChatMessage = user.getFriendChatMessage();
                JSONArray jsonArray;
                if (friendChatMessage == null || friendChatMessage.isEmpty()) {
                    jsonArray = new JSONArray();
                    jsonArray = jsonArray.put(needInsertMessageJSONObject);
                } else {
                    try {
                        jsonArray = new JSONArray(friendChatMessage);
                        jsonArray.put(needInsertMessageJSONObject);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                updateFriendChatMessages(jyId, jsonArray.toString());
            }
        }).start();
    }


}
