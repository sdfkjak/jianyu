package com.example.mychatapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.network.MessageHub;

public class SearchFriendViewModel extends ViewModel {
    private MessageHub messageHub;
    public SearchFriendViewModel(){ messageHub = MessageHub.getInstance(); }

    public LiveData<User> getSearchUser(){
        return messageHub.getWSQueryUser();
    }
}
