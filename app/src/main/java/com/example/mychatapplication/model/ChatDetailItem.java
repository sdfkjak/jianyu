package com.example.mychatapplication.model;

public class ChatDetailItem {
    public enum ChatType {
        MYCHAT, OTHERCHAT, TIME;
    }
    private String type;
    private String messageType;
    private String content;
    private long timestamp;
    private int imgWidth;
    private int imgHeight;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public ChatDetailItem(String type, String content, long timestamp) {
        this.type = type;
        this.messageType = "TEXT";
        this.content = content;
        this.timestamp = timestamp;
    }

    public ChatDetailItem(String type, long timestamp, int imgWidth, int imgHeight) {
        this.type = type;
        this.messageType = "IMAGE";
        this.timestamp = timestamp;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public ChatDetailItem(String type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }
}
