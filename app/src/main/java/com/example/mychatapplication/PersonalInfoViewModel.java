package com.example.mychatapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;

public class PersonalInfoViewModel extends AndroidViewModel {
    private UserInfoRepository userInfoRepository;

    public PersonalInfoViewModel(@NonNull Application application) {
        super(application);
        this.userInfoRepository = new UserInfoRepository(application);
    }
    public LiveData<UserInfo> getCurrentUserInfoLiveData(String jyId) {
        return userInfoRepository.getCurrentUserInfoLiveData(jyId);
    }
    public void updateUserInfo(UserInfo... userInfos){
        userInfoRepository.updateUserInfo(userInfos);
    }
}
