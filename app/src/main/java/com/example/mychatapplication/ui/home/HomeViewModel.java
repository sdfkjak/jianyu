package com.example.mychatapplication.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private UserInfoRepository userInfoRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.userInfoRepository = new UserInfoRepository(application);
    }

    public LiveData<List<UserInfo>> getFriendChatMessageList(){
        return userInfoRepository.getAllUserInfoLiveData();
    }
}