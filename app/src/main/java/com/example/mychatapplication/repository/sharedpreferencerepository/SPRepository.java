package com.example.mychatapplication.repository.sharedpreferencerepository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;

public class SPRepository {
    private static SPRepository spRepository;
    private SPDao spDao;
    private SPRepository(){ spDao = SPDao.getInstance(); }
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
        SPDao.getInstance().saveLoginMsg(isLogin, jyId);
    }

    public void getLoginMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginMsgLiveData.postValue(SPDao.getInstance().getLoginMsg());
            }
        }).start();
    }

    public float getSoftKeyboardHeight(){
        return spDao.getSoftKeyboardHeight();
    }

    public void setSoftKeyboardHeight(float height){
        if(getSoftKeyboardHeight() == 0.0f){
            spDao.setSoftKeyboardHeight(height);
        }
    }
}
