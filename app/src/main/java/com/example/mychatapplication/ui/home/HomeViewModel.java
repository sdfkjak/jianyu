package com.example.mychatapplication.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private UserRepository userRepository;
    public HomeViewModel() {
        super();
        this.userRepository = new UserRepository();
    }

    public LiveData<List<User>> getAllUserLiveData(){
        return userRepository.getAllUserLiveData();
    }

}