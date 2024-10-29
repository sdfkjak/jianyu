package com.example.mychatapplication.commomclass.friendapplication;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class ReceiveFriendRequest {
    private String friendRequestsId;
    private String mode;
    @SerializedName("user_info")
    private CommonUserInfo commonUserInfo;
    @SerializedName("message")
    private FriendRequestMessage[] friendRequestMessage;
    private ArrayList<FriendRequestMessage> friendRequestMessageArrayList;

    public String getFriendRequestsId() {
        return friendRequestsId;
    }

    public String getMode() {
        return mode;
    }

    public CommonUserInfo getCommonUserInfo() {
        return commonUserInfo;
    }

    public ArrayList<FriendRequestMessage> getFriendRequestMessageArrayList() {
        return friendRequestMessageArrayList;
    }

    public void setCommonUserInfo(CommonUserInfo commonUserInfo) {
        this.commonUserInfo = commonUserInfo;
    }

    public ReceiveFriendRequest(String friendRequestsId, String mode, CommonUserInfo commonUserInfo, FriendRequestMessage[] friendRequestMessage) {
        this.friendRequestsId = friendRequestsId;
        this.mode = mode;
        this.commonUserInfo = commonUserInfo;
        this.friendRequestMessage = friendRequestMessage;
        this.friendRequestMessageArrayList = new ArrayList<>(Arrays.asList(friendRequestMessage));
    }
    public ReceiveFriendRequest(String friendRequestsId, String mode, CommonUserInfo commonUserInfo) {
        this.friendRequestsId = friendRequestsId;
        this.mode = mode;
        this.commonUserInfo = commonUserInfo;
    }
    public ReceiveFriendRequest(CommonUserInfo commonUserInfo) {
        this.commonUserInfo = commonUserInfo;
    }
}
