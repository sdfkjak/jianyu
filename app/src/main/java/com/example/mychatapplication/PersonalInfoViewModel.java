package com.example.mychatapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SDcardRepository.SDCardRepository;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

public class PersonalInfoViewModel extends ViewModel {
    private UserRepository userRepository;
    private SDCardRepository sdCardRepository;

    public PersonalInfoViewModel() {
        super();
        this.userRepository = new UserRepository();
        this.sdCardRepository = SDCardRepository.getInstance();
    }

    public LiveData<User> getUserLiveData(String jyId) {
        return userRepository.getUserLiveData(jyId);
    }

    public void saveAvatar(String fileName, byte[] bytes){
        sdCardRepository.saveAvatar(fileName, bytes);
    }
}
