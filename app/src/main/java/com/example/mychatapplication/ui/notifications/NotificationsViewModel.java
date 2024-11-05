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
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

public class NotificationsViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public LiveData<User> getUserLiveData(String jyId) {
        return userRepository.getUserLiveData(jyId);
    }
    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository();
    }
}