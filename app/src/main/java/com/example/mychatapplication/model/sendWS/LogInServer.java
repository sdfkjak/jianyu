package com.example.mychatapplication.model.sendWS;

import com.example.mychatapplication.MainApplication;

public class LogInServer {
    private String type = "LOGINSERVER";
    private String source = MainApplication.getInstance().user.getJyId();
}
