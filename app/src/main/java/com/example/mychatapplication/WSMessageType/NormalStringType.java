package com.example.mychatapplication.WSMessageType;

public enum NormalStringType {
    RESPONSE_TIMESTAMP;
    public static boolean contain(String value){
        for (NormalStringType normalStringType: NormalStringType.values()){
            if(normalStringType.name().equalsIgnoreCase(value)){
                return true;
            }
        }
        return false;
    }
}

