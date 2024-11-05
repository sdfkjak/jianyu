package com.example.mychatapplication.repository.SQLiteRepository;

import androidx.lifecycle.LiveData;

import com.example.mychatapplication.model.User;

import java.util.List;

public class UserRepository {
    private UserDao userDao;
    public UserRepository(){
        UserDatabase userDatabase = UserDatabase.getInstance();
        userDao = userDatabase.getUserDao();
    }

    public void insertUser(User user){
        new Thread(() -> userDao.insertUser(user)).start();
    }

    public void updateUser(String jyId, String sex, String area, String nickname, String friendChatId){
        new Thread(() -> userDao.updateUser(jyId, sex, area, nickname, friendChatId)).start();
    }

    public void updateFriendChatMessage(String jyId, String friendChatMessage){
        new Thread(() -> userDao.updateFriendChatMessage(jyId, friendChatMessage)).start();
    }

    public LiveData<User> getUserLiveData(String jyId){ return userDao.getUserLiveData(jyId); }

    public User getUser(String jyId){ return userDao.getUser(jyId); }

    public LiveData<List<User>> getAllUserLiveData(){ return userDao.getAllUserLiveData(); }

    public List<User> getAllUser(){ return userDao.getAllUser(); }
    public boolean userExist(String jyid){ return userDao.userExist(jyid); }
}
