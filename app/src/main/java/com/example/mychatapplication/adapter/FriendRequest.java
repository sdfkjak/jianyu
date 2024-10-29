package com.example.mychatapplication.adapter;

import android.graphics.Bitmap;

public class FriendRequest {
    private String nickName;
    private String applyMeg;
    private Bitmap avatar;
    private String jyId;
    private boolean isMyReq;
    private boolean hasAdd;

    public FriendRequest(String nickName, String applyMeg, Bitmap avatar, String jyId, boolean isMyReq, boolean hasAdd) {
        this.nickName = nickName;
        this.applyMeg = applyMeg;
        this.avatar = avatar;
        this.jyId = jyId;
        this.isMyReq = isMyReq;
        this.hasAdd = hasAdd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getApplyMeg() {
        return applyMeg;
    }

    public void setApplyMeg(String applyMeg) {
        this.applyMeg = applyMeg;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getJyId() {
        return jyId;
    }

    public void setJyId(String jyId) {
        this.jyId = jyId;
    }

    public boolean isMyReq() {
        return isMyReq;
    }

    public void setMyReq(boolean myReq) {
        isMyReq = myReq;
    }

    public boolean isHasAdd() {
        return hasAdd;
    }

    public void setHasAdd(boolean hasAdd) {
        this.hasAdd = hasAdd;
    }

}
