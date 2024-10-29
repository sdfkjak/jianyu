package com.example.mychatapplication.websocketsend;

import com.example.mychatapplication.MainApplication;

public class QueryUserClass {
    private String type = "QUERYUSER";
    private String source = MainApplication.getInstance().user.jyId;
    private String searchString;

    public QueryUserClass(String searchString) {
        this.searchString = searchString;
    }
}
