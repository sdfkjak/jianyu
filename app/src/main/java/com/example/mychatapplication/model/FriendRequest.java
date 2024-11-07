package com.example.mychatapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FriendRequest {

    private String friendRequestsId;
    private String mode;
    @SerializedName("user_info")
    private User user;

    private String sendMessage;
    @SerializedName("message")
    private ArrayList<ChatMessage> messageArrayList;


    public String getFriendRequestsId() {
        return friendRequestsId;
    }

    public void setFriendRequestsId(String friendRequestsId) {
        this.friendRequestsId = friendRequestsId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<ChatMessage> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<ChatMessage> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }
}
