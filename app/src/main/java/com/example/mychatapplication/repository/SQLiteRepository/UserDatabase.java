package com.example.mychatapplication.repository.SQLiteRepository;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.model.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase userDatabase;

    public synchronized static UserDatabase getInstance(){
        if(userDatabase == null){
            userDatabase = Room.databaseBuilder(MainApplication.applicationContext, UserDatabase.class, MainApplication.USERDATABASE + MainApplication.getInstance().user.jyId).build();
        }
        return userDatabase;
    }
    public abstract UserDao getUserDao();

    public void clearUserDatabase(){ userDatabase = null; }
}
