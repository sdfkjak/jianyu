package com.example.mychatapplication.commomclass;

public class TalkMessage {
    public long timestamp;
    public String content;
    public TalkMessage(long timestamp, String content){
        this.timestamp = timestamp;
        this.content = content;
    }
    public static Long covLong(String numString){
        return Long.parseLong(numString);
    }
}
