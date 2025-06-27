package com.example.mychatapplication;

import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewModel extends ViewModel {
    private ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private UserRepository userRepository;
    public LoginViewModel(){ userRepository = new UserRepository();}
    public interface NavigateCallback{
        void onReadyToNavigate(User user);
        void onError(Exception e);
    }
    public void initUserAndNavigate(String jyId, NavigateCallback callback){
        dbExecutor.execute(() -> {
            try{
                User user = getUser(jyId);
                callback.onReadyToNavigate(user);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }
    public User getUser(String jyId){
        return userRepository.getUser(jyId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        dbExecutor.shutdown();
    }
}
