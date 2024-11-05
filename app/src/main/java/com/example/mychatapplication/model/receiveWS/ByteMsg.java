package com.example.mychatapplication.model.receiveWS;

public class ByteMsg {
    private String type;
    private String source;
    private String timestamp;
    private byte[] byteMsg;

    public ByteMsg(String type, String source, String timestamp, byte[] byteMsg) {
        this.type = type;
        this.source = source;
        this.timestamp = timestamp;
        this.byteMsg = byteMsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getByteMsg() {
        return byteMsg;
    }

    public void setByteMsg(byte[] byteMsg) {
        this.byteMsg = byteMsg;
    }
}
