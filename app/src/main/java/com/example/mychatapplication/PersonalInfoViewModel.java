package com.example.mychatapplication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SDcardRepository.SDCardRepository;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

public class PersonalInfoViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private SDCardRepository sdCardRepository;

    public PersonalInfoViewModel(@NonNull Application application) {
        super(application);
        this.userRepository = new UserRepository();
        this.sdCardRepository = SDCardRepository.getInstance();
    }
    public LiveData<User> getUserLiveData(String jyId) {
        return userRepository.getUserLiveData(jyId);
    }

    public void saveAvatar(String fileName, byte[] bytes){
        sdCardRepository.saveAvatar(fileName, bytes);
    }
//    public void updateUserInfo(UserInfo... userInfos){
//        userInfoRepository.updateUserInfo(userInfos);
//    }
}
