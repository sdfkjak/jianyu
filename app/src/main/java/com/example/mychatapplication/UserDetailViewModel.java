package com.example.mychatapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

public class UserDetailViewModel extends ViewModel {
    private UserRepository userRepository;
    public UserDetailViewModel(){
        userRepository = new UserRepository();
    }
    public LiveData<User> getUserLiveData(String jyId){
        return userRepository.getUserLiveData(jyId);
    }
}
