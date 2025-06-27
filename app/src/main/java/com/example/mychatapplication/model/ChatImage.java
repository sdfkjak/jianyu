package com.example.mychatapplication.model;

public class ChatImage {
    private String chatId;
    private long timestamp;
    private byte[] imgByte;

    public ChatImage(String chatId, long timestamp, byte[] imgByte) {
        this.chatId = chatId;
        this.timestamp = timestamp;
        this.imgByte = imgByte;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getImgByte() {
        return imgByte;
    }

    public void setImgByte(byte[] imgByte) {
        this.imgByte = imgByte;
    }
}
