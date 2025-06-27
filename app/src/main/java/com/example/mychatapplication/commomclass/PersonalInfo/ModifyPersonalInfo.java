package com.example.mychatapplication.commomclass.PersonalInfo;

import com.example.mychatapplication.MainApplication;

public class ModifyPersonalInfo {
    private String type = "MODIFYPERSONALINFO";
    private String source = MainApplication.getInstance().user.getJyId();
    private String item;
    private String value;

    public ModifyPersonalInfo(String item, String value) {
        this.item = item;
        this.value = value;
    }
}
