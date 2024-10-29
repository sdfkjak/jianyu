package com.example.mychatapplication.commomclass.PrivateChat;

import com.example.mychatapplication.MainApplication;

import java.util.Date;

public class FriendChatMessage {
    private String source;
    private String type;
    private String content;
    private long timestamp;

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public FriendChatMessage(String content) {
        this.source = MainApplication.getInstance().user.jyId;
        this.type = "TEXT";
        this.content = content;
        this.timestamp = (new Date()).getTime();
    }
    public FriendChatMessage(String content, long sendTimestamp) {
        this.source = MainApplication.getInstance().user.jyId;
        this.type = "TEXT";
        this.content = content;
        this.timestamp = sendTimestamp;
    }
    public FriendChatMessage(String source, String content) {
        this.source = source;
        this.type = "TEXT";
        this.content = content;
        this.timestamp = (new Date()).getTime();
    }

    public FriendChatMessage(String source, String type, long timestamp) {
        this.source = source;
        this.type = type;
        this.timestamp = timestamp;
    }
}
