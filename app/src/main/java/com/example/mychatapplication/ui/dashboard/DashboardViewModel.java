package com.example.mychatapplication.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {
    private UserInfoRepository userInfoRepository;
    private MutableLiveData<List<Integer>> integerMutableLiveData = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<Integer>> getIntegerMutableLiveData() {
        return integerMutableLiveData;
    }

    public DashboardViewModel(Application application) {
        super(application);
        this.userInfoRepository = new UserInfoRepository(application);
        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(0);
        this.integerMutableLiveData.setValue(integers);
    }
    public LiveData<List<UserInfo>> getAllUserInfoLiveData(){
        return userInfoRepository.getAllUserInfoLiveData();
    }
    public List<UserInfo> getAllUserInfo(){
        return userInfoRepository.getAllUserInfo();
    }

    public UserInfo getUserInfo(String jyId){
        return userInfoRepository.getCurrentUserInfo(jyId);
    }

    public LiveData<UserInfo> getUserInfoLiveData(String jyId){
        return userInfoRepository.getCurrentUserInfoLiveData(jyId);
    }
}