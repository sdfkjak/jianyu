package com.example.mychatapplication;

import androidx.lifecycle.ViewModel;

import com.example.mychatapplication.model.User;
import com.example.mychatapplication.repository.SQLiteRepository.UserDao;
import com.example.mychatapplication.repository.SQLiteRepository.UserRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LaunchViewModel extends ViewModel {
    private ExecutorService dbExecute = Executors.newSingleThreadExecutor();
    private UserRepository userRepository;
    public LaunchViewModel(){userRepository = new UserRepository();}
    public interface Callback{
        void onSucceed(User user);
        void onError(Exception e);
    }

    public void setUserAndNavigate(String jyId, Callback callback){
        dbExecute.execute(() -> {
            try {
                User user = getUser(jyId);
                callback.onSucceed(user);
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
        dbExecute.shutdown();
    }
}
