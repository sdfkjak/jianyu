package com.example.mychatapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserInfoDao {
    @Insert
    void insertUserInfo(UserInfo... userInfo);
    @Update
    void updateUserInfo(UserInfo... userInfo);
    @Query("UPDATE UserInfo SET avatar = :avatar WHERE jyId = :jyId")
    void updateAvatar(String jyId, String avatar);
    @Query("UPDATE UserInfo SET nickname = :nickname WHERE jyId = :jyId")
    void updateNickname(String jyId, String nickname);
    @Query("UPDATE UserInfo SET sex = :sex WHERE jyId = :jyId")
    void updateSex(String jyId, String sex);
    @Query("UPDATE UserInfo SET area = :area WHERE jyId = :jyId")
    void updateArea(String jyId, String area);
    @Query("UPDATE UserInfo SET friendChatId = :friendChatId WHERE jyId = :jyId")
    void updateFriendChatId(String jyId, String friendChatId);
    @Query("UPDATE UserInfo SET friendChatMessages = :friendChatMessages WHERE jyId = :jyId")
    void updateFriendChatMessages(String jyId, String friendChatMessages);
    @Delete
    void deleteUserInfo(UserInfo... userInfo);
    @Query("DELETE FROM UserInfo")
    Void deleteAllUserInfo();
    @Query("SELECT * FROM UserInfo")
    LiveData<List<UserInfo>> getAllUserInfoLiveData();
    @Query("SELECT * FROM UserInfo")
    List<UserInfo> getAllUserInfo();
    @Query("SELECT * FROM UserInfo WHERE jyId = :jyId")
    LiveData<UserInfo> getUserInfoLiveData(String jyId);
    @Query("SELECT * FROM UserInfo WHERE jyId = :jyId")
    UserInfo getUserInfo(String jyId);
    @Query("SELECT EXISTS(SELECT 1 FROM UserInfo WHERE jyId = :jyId)")
    boolean userExist(String jyId);
}
