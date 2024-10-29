package com.example.mychatapplication.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mychatapplication.MainApplication;

@Database(entities = {UserInfo.class}, version = 1, exportSchema = false)
public abstract class UserInfoDatabase extends RoomDatabase {
    private static UserInfoDatabase userInfoDatabaseINSTANCE;
    public static synchronized UserInfoDatabase getDatabase(Context context){
        if(userInfoDatabaseINSTANCE == null){
            userInfoDatabaseINSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserInfoDatabase.class, MainApplication.USERINFODATABASE + MainApplication.getInstance().user.jyId).build();
        }
        return userInfoDatabaseINSTANCE;
    }
    public abstract UserInfoDao getUserInfoDao();

    public void clearUserInfoDatabase(){
        userInfoDatabaseINSTANCE = null;
    }
}
