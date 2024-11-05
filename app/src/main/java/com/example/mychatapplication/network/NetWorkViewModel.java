package com.example.mychatapplication.network;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SDcardRepository.SDCardRepository;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

import java.util.List;

public class NetWorkViewModel{
    private static NetWorkViewModel netWorkViewModel;
    private UserRepository userRepository;
    private SDCardRepository sdCardRepository;
    private NetWorkViewModel(){
        userRepository = new UserRepository();
        sdCardRepository = SDCardRepository.getInstance();
    };
    public synchronized static NetWorkViewModel getInstance(){
        if(netWorkViewModel == null){
            netWorkViewModel = new NetWorkViewModel();
        }
        return netWorkViewModel;
    }
    public User getUser(String jyId){
        return userRepository.getUser(jyId);
    }
    public boolean getUserExist(String jyid){ return userRepository.userExist(jyid); }
    public void insertUser(User user){
        userRepository.insertUser(user);
    }
    public void updateUser(String jyId, String sex, String area, String nickname, String friendChatId){ userRepository.updateUser(jyId, sex, area, nickname, friendChatId); }
    public void saveAvatar(String fileName, byte[] bytes){
        sdCardRepository.saveAvatar(fileName, bytes);
    }

    public void saveCache(String fileName, byte[] bytes){
        sdCardRepository.saveCache(fileName, bytes);
    }
}
