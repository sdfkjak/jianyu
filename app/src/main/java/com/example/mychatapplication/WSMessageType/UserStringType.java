package com.example.mychatapplication.WSMessageType;

public enum UserStringType {
    USERINIT, FRIENDAPPLICATIONINIT, FRIENDAPPLICATION, QUERYUSERRESULT, ADDFRIENDINFO, FRIENDINIT, FRIEND_CHAT, FRIENDCHATINIT,IMAGE;

    public static boolean contain(String value){
        for (UserStringType userStringType: UserStringType.values()){
            if(userStringType.name().equalsIgnoreCase(value)){
                return true;
            }
        }
        return false;
    }
}
