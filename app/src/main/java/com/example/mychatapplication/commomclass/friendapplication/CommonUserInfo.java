package com.example.mychatapplication.commomclass.friendapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CommonUserInfo implements Serializable {
    @SerializedName("user_id")
    private String jyId;
    @SerializedName("user_nickname")
    private String nickname;
    @SerializedName("user_sex")
    private String sex;

    private Bitmap avatar;
    private byte[] avatarBytes;
    @SerializedName("user_area")
    private String area;

    public String getJyId() {
        return jyId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSex() {
        return sex;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public void setAvatarBytes(byte[] avatarBytes) {
        this.avatarBytes = avatarBytes;
    }

    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    public String getArea() {
        return area;
    }

    public CommonUserInfo(String jyId, String nickname, String sex, Bitmap avatar, String area) {
        this.jyId = jyId;
        this.nickname = nickname;
        this.sex = sex;
        this.avatar = avatar;
        this.area = area;
    }

    public CommonUserInfo(String jyId, String nickname, String sex, String area) {
        this.jyId = jyId;
        this.nickname = nickname;
        this.sex = sex;
        this.area = area;
    }
    public CommonUserInfo(String jyId, byte[] avatarBytes) {
        this.jyId = jyId;
        this.avatarBytes = avatarBytes;
    }
}
