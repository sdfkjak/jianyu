package com.example.mychatapplication.memory;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mychatapplication.model.ChatImage;

import java.util.ArrayList;

public class ChatImageCacheManager {
    private final static String tag = "ChatImageCacheManager";
    private static ChatImageCacheManager chatImageCacheManager;
    private MutableLiveData<ArrayList<ChatImage>> chatImageArrayListMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private ChatImageCacheManager(){}
    public synchronized static ChatImageCacheManager getInstance(){
        if(chatImageCacheManager == null){
            chatImageCacheManager = new ChatImageCacheManager();
        }
        return chatImageCacheManager;
    }

    public LiveData<ArrayList<ChatImage>> getChatImageArrayListMutableLiveData(){
        return chatImageArrayListMutableLiveData;
    }

    public void addToCache(ChatImage chatImage){
        ArrayList<ChatImage> chatImageArrayList = chatImageArrayListMutableLiveData.getValue();
        assert chatImageArrayList != null;
        chatImageArrayList.add(chatImage);
        Log.d("流程", "设置值");
        Log.d("流程", "设置的值： " + chatImage.getChatId() + " 时间戳: " + chatImage.getTimestamp());
        chatImageArrayListMutableLiveData.postValue(chatImageArrayList);
    }

    public void flush(){
        chatImageArrayListMutableLiveData.postValue(chatImageArrayListMutableLiveData.getValue());
    }

    public ChatImage getChatImage(String jyId, long timestamp){
        for (int i = 0; i < chatImageArrayListMutableLiveData.getValue().size(); i++) {
            if(chatImageArrayListMutableLiveData.getValue().get(i).getChatId().equals(jyId) && chatImageArrayListMutableLiveData.getValue().get(i).getTimestamp() == timestamp){
                return chatImageArrayListMutableLiveData.getValue().get(i);
            }
        }
        return null;
    }

    public void deleteFromCache(ChatImage chatImage){
        chatImageArrayListMutableLiveData.getValue().remove(chatImage);
    }
    public void deleteFromCache(String jyId, long timestamp){
        for (int i = 0; i < chatImageArrayListMutableLiveData.getValue().size(); i++) {
            if(chatImageArrayListMutableLiveData.getValue().get(i).getChatId().equals(jyId) && chatImageArrayListMutableLiveData.getValue().get(i).getTimestamp() == timestamp){
                chatImageArrayListMutableLiveData.getValue().remove(i);
                break;
            }
        }
    }
}
