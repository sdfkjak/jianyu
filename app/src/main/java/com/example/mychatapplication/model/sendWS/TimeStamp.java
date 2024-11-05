package com.example.mychatapplication.model.sendWS;

import com.example.mychatapplication.MainApplication;

public class TimeStamp {
    private String type = "GETTIMESTAMP";
    private String source = MainApplication.getInstance().user.jyId;
    private long timestamp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
