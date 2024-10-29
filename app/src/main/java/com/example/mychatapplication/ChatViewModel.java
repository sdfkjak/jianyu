package com.example.mychatapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;

public class ChatViewModel extends AndroidViewModel {
    private LiveData<UserInfo> myUserInfo, targetUserInfo;
    private UserInfoRepository userInfoRepository;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        userInfoRepository = new UserInfoRepository(application);
    }

    public LiveData<UserInfo> getUserInfo(String jyId){
        return userInfoRepository.getCurrentUserInfoLiveData(jyId);
    }
    public LiveData<UserInfo> getCurrentUserInfoLiveData(){
        return userInfoRepository.getCurrentUserInfoLiveData(MainApplication.getInstance().user.jyId);
    }
    public LiveData<UserInfo> getUserInfoLiveData(String jyid){
        return userInfoRepository.getCurrentUserInfoLiveData(jyid);
    }
}
