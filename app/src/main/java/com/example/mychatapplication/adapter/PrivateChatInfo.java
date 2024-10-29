package com.example.mychatapplication.adapter;

public class PrivateChatInfo {
    private boolean isMe;
    private long timestamp;
    private String type;
    private String content;

    public String getType() {
        return type;
    }

    public PrivateChatInfo(boolean isMe, long timestamp, String content) {
        this.isMe = isMe;
        this.timestamp = timestamp;
        this.content = content;
        this.type = "TEXT";
    }

    public PrivateChatInfo(boolean isMe, String type, long timestamp) {
        this.isMe = isMe;
        this.timestamp = timestamp;
        this.type = type;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
