package com.example.mychatapplication.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//@Entity(indices = {@Index(value = "phone", unique = true)})
@Entity
public class User implements Serializable {
    @PrimaryKey()
    @NonNull
    @SerializedName("user_id")
    private String jyId;
    @SerializedName("user_nickname")
    private String nickname;
    @SerializedName("user_sex")
    private String sex;
    @SerializedName("user_area")
    private String area;
    private String friendChatId;

    private String friendChatMessage;

    @NonNull
    public String getJyId() {
        return jyId;
    }

    public void setJyId(@NonNull String jyId) {
        this.jyId = jyId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFriendChatId() {
        return friendChatId;
    }

    public void setFriendChatId(String friendChatId) {
        this.friendChatId = friendChatId;
    }

    public String getFriendChatMessage() {
        return friendChatMessage;
    }

    public void setFriendChatMessage(String friendChatMessage) {
        this.friendChatMessage = friendChatMessage;
    }
}

