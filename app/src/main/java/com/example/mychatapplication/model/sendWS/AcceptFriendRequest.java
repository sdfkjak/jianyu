package com.example.mychatapplication.model.sendWS;

import java.util.Date;

public class AcceptFriendRequest {
    private String type = "ACCEPTFRIENDREQUEST";
    private String friendRequestId;
    private String source;
    private String target;
    private Long timestamp;

    public AcceptFriendRequest(String friendRequestId, String source, String target) {
        this.friendRequestId = friendRequestId;
        this.source = source;
        this.target = target;
        this.timestamp = (new Date()).getTime();
    }
}
