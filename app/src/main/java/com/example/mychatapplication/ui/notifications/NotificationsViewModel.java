package com.example.mychatapplication.ui.notifications;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoDao;
import com.example.mychatapplication.database.UserInfoDatabase;
import com.example.mychatapplication.database.UserInfoRepository;

public class NotificationsViewModel extends AndroidViewModel {
    private UserInfoRepository userInfoRepository;

    public LiveData<UserInfo> getCurrentUserInfoLiveData(String jyId) {
        return userInfoRepository.getCurrentUserInfoLiveData(jyId);
    }
    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        userInfoRepository = new UserInfoRepository(application);
    }
    void updateUserInfo(UserInfo...userInfos){
        userInfoRepository.updateUserInfo(userInfos);
    }
}