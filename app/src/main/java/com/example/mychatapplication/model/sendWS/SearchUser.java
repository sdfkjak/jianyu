package com.example.mychatapplication.model.sendWS;

import com.example.mychatapplication.MainApplication;

public class SearchUser {
    private String type = "QUERYUSER";
    private String source = MainApplication.getInstance().user.jyId;
    private String searchString;

    public SearchUser(String searchString) {
        this.searchString = searchString;
    }
}
