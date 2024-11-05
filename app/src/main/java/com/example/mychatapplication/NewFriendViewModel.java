package com.example.mychatapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.FriendRequest;
import com.example.mychatapplication.network.MessageHub;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

import java.util.ArrayList;
import java.util.Objects;

public class NewFriendViewModel extends ViewModel {
    private final static String tag = "NewFriendViewModel";
    private MessageHub messageHub;
    private UserRepository userRepository;
    private MutableLiveData<ArrayList<FriendRequest>> friendRequestArrayListMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    public NewFriendViewModel(){
        userRepository = new UserRepository();
        messageHub = MessageHub.getInstance();
    }

    public LiveData<FriendRequest[]> getWSFriendApplicationInit(){
        return messageHub.getWSFriendApplicationInitLiveData();
    }

    public void addFriendRequest(FriendRequest[] friendRequests){
        for (FriendRequest friendRequest : friendRequests) {
            friendRequestArrayListMutableLiveData.getValue().add(friendRequest);
        }
        friendRequestArrayListMutableLiveData.postValue(friendRequestArrayListMutableLiveData.getValue());
    }
    public LiveData<ArrayList<FriendRequest>> getFriendRequestArrayListLiveData(){
        return friendRequestArrayListMutableLiveData;
    }
    public boolean getUserExist(String jyid){ return userRepository.userExist(jyid); }
}
