package com.example.mychatapplication.ui.dashboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public ContactViewModel(Application application) {
        super(application);
        this.userRepository = new UserRepository();
    }
    public LiveData<List<User>> getAllUserLiveData(){
        return userRepository.getAllUserLiveData();
    }
}