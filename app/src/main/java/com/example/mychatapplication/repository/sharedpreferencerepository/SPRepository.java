package com.example.mychatapplication.repository.sharedpreferencerepository;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

public class SPRepository {
    private static SPRepository spRepository;
    private SPRepository(){}
    public synchronized static SPRepository getInstance(){
        if(spRepository == null){
            spRepository = new SPRepository();
        }
        return spRepository;
    }

    private MutableLiveData<HashMap<String, String>> loginMsgLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSaveLoginMsgOver = new MutableLiveData<>();

    public MutableLiveData<HashMap<String, String>> getLoginMsgLiveData() {
        return loginMsgLiveData;
    }

    public MutableLiveData<Boolean> getIsSaveLoginMsgOver() {
        return isSaveLoginMsgOver;
    }

    public void saveLoginMsg(boolean isLogin, String jyId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                isSaveLoginMsgOver.postValue(SPDao.getInstance().saveLoginMsg(isLogin, jyId));
            }
        }).start();
    }

    public void getLoginMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginMsgLiveData.postValue(SPDao.getInstance().getLoginMsg());
            }
        }).start();
    }
}
