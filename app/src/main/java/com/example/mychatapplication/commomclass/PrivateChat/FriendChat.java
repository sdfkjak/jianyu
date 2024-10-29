package com.example.mychatapplication.commomclass.PrivateChat;

import com.example.mychatapplication.MainApplication;

public class FriendChat {
    private String type = "FRIENDCHAT";
    private String friendChatId;
    private String source = MainApplication.getInstance().user.jyId;
    private String target;
    private FriendChatMessage friendChatMessage;

    public String getFriendChatId() {
        return friendChatId;
    }

    public String getTarget() {
        return target;
    }

    public FriendChatMessage getFriendChatMessage() {
        return friendChatMessage;
    }

    public FriendChat(String friendChatId, String target, FriendChatMessage friendChatMessage) {
        this.friendChatId = friendChatId;
        this.target = target;
        this.friendChatMessage = friendChatMessage;
    }
}
