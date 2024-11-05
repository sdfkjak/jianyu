package com.example.mychatapplication.model;

import com.example.mychatapplication.MainApplication;

public class ChatMessage {
    private String source;

    private String type;

    private String content;

    private long timestamp;

    private Integer width;

    private Integer height;

    public ChatMessage(String source, String content, long timestamp) {
        this.source = source;
        this.type = "TEXT";
        this.content = content;
        this.timestamp = timestamp;
    }
    public ChatMessage(String source, int width, int height, long timestamp) {
        this.source = source;
        this.type = "IMAGE";
        this.width = width;
        this.height = height;
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
