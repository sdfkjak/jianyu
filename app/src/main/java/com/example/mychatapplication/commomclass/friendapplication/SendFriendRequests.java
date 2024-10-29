package com.example.mychatapplication.commomclass.friendapplication;

import com.example.mychatapplication.MainApplication;

import java.util.Date;

public class SendFriendRequests {
    private String type = "FRIENDREQUEST";
    private String source = MainApplication.getInstance().user.jyId;
    private String target;
    private FriendRequestMessage message;

    public String getSource() {
        return source;
    }

    public SendFriendRequests(String target, String content) {
        this.target = target;
        this.message = new FriendRequestMessage(this.source, (new Date()).getTime(), content);
    }
    public SendFriendRequests(String target) {
        this.target = target;
    }
}
