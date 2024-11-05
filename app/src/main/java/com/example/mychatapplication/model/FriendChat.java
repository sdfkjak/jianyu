package com.example.mychatapplication.model;

import com.example.mychatapplication.MainApplication;

public class FriendChat {
    private String type = "FRIENDCHAT";
    private String friendChatId;
    private String source = MainApplication.getInstance().user.jyId;
    private String target;
    private ChatMessage chatMessage;

    public String getFriendChatId() {
        return friendChatId;
    }

    public String getTarget() {
        return target;
    }

    public ChatMessage getFriendChatMessage() {
        return chatMessage;
    }

    public FriendChat(String friendChatId, String target, ChatMessage chatMessage) {
        this.friendChatId = friendChatId;
        this.target = target;
        this.chatMessage = chatMessage;
    }
}
