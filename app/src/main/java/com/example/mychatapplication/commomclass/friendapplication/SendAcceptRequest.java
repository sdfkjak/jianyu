package com.example.mychatapplication.commomclass.friendapplication;

import java.util.Date;

public class SendAcceptRequest {
    private String type = "ACCEPTFRIENDREQUEST";
    private String friendRequestId;
    private String source;
    private String target;
    private Long timestamp;

    public SendAcceptRequest(String friendRequestId, String source, String target) {
        this.friendRequestId = friendRequestId;
        this.source = source;
        this.target = target;
        this.timestamp = (new Date()).getTime();
    }
}
