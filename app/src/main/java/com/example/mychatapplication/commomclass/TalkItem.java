package com.example.mychatapplication.commomclass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TalkItem{
    public String nickname, time, talkMeg;
    public TalkItem(String nickname, String time, String talkMeg){
        this.nickname = nickname;
        this.time = time;
        this.talkMeg = talkMeg;

    }
}