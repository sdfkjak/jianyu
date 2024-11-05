package com.example.mychatapplication.model.sendWS;

public class InitClient {
    private String type = "INITCLIENT";
    private String jyId;
    private boolean needInit;

    public InitClient(String jyId, boolean needInit) {
        this.jyId = jyId;
        this.needInit = needInit;
    }

    public String getType() {
        return type;
    }

    public String getJyId() {
        return jyId;
    }

    public void setJyId(String jyId) {
        this.jyId = jyId;
    }

    public boolean isNeedInit() {
        return needInit;
    }

    public void setNeedInit(boolean needInit) {
        this.needInit = needInit;
    }
}
