package com.example.mychatapplication.model;

public class ChatImage {
    private String jyId;
    private long timestamp;
    private byte[] imgByte;

    public ChatImage(String jyId, long timestamp, byte[] imgByte) {
        this.jyId = jyId;
        this.timestamp = timestamp;
        this.imgByte = imgByte;
    }

    public String getJyId() {
        return jyId;
    }

    public void setJyId(String jyId) {
        this.jyId = jyId;
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
