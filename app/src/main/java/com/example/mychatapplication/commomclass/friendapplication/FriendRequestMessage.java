package com.example.mychatapplication.commomclass.friendapplication;

public class FriendRequestMessage {
    private String source;
    private long timestamp;
    private String content;

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    public FriendRequestMessage(String source, long timestamp, String content) {
        this.source = source;
        this.timestamp = timestamp;
        this.content = content;
    }
    public FriendRequestMessage(String source, String content) {
        this.source = source;
        this.content = content;
    }
}
