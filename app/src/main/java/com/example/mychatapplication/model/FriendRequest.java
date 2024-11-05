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
    private ArrayList<String> messageArrayList;

    public FriendRequest(String friendRequestsId, String mode, User user, String message) {
        this.friendRequestsId = friendRequestsId;
        this.mode = mode;
        this.user = user;
        this.sendMessage = message;
    }

    public FriendRequest(String friendRequestsId, String mode, User user, ArrayList<String> messageArrayList) {
        this.friendRequestsId = friendRequestsId;
        this.mode = mode;
        this.user = user;
        this.messageArrayList = messageArrayList;
    }

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

    public String getSendMessage() {
        return sendMessage;
    }

    public void setSendMessage(String sendMessage) {
        this.sendMessage = sendMessage;
    }

    public ArrayList<String> getMessageArrayList() {
        return messageArrayList;
    }

    public void setMessageArrayList(ArrayList<String> messageArrayList) {
        this.messageArrayList = messageArrayList;
    }
}
