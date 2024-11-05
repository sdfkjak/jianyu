package com.example.mychatapplication.repository.SQLiteRepository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.mychatapplication.model.User;

import java.util.List;

@Dao
public interface UserDao {
    //onConflict参数用于指定当插入操作发生冲突（例如主键冲突）时的策略，OnConflictStrategy.REPLACE表示替换冲突的行。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("UPDATE User SET jyId = :jyId, sex = :sex, area = :area, nickname = :nickname, friendChatId = :friendChatId WHERE jyId = :jyId")
    void updateUser(String jyId, String sex, String area, String nickname, String friendChatId);

    @Query("UPDATE User SET friendChatMessage = :friendChatMessage WHERE jyId = :jyId")
    void updateFriendChatMessage(String jyId, String friendChatMessage);

    @Query("SELECT * FROM User WHERE jyId = :jyId")
    LiveData<User> getUserLiveData(String jyId);

    @Query("SELECT * FROM User WHERE jyId = :jyId")
    User getUser(String jyId);

    @Query("SELECT * FROM User")
    LiveData<List<User>> getAllUserLiveData();

    @Query("SELECT * FROM User")
    List<User> getAllUser();

    @Query("SELECT EXISTS(SELECT 1 FROM User WHERE jyId = :jyId)")
    boolean userExist(String jyId);
}
