package com.example.mychatapplication.network;

import com.example.mychatapplication.WSMessageType.NormalStringType;
import com.example.mychatapplication.WSMessageType.UserStringType;
import com.example.mychatapplication.model.receiveWS.ByteMsg;

import org.json.JSONException;

import java.util.HashMap;

public class DistributionHub {
    private static DistributionHub distributionHub;
    private DistributionHub(){}
    public static synchronized DistributionHub getInstance(){
        if(distributionHub == null){
            distributionHub = new DistributionHub();
        }
        return distributionHub;
    }

    public void distributeStringMessage(HashMap<String, String> msg) throws JSONException {
        if(UserStringType.contain(msg.get("type"))){
            MessageHub.getInstance().receiveStringMsg(msg);
        } else if (NormalStringType.contain(msg.get("type"))) {
            NormalMsgProcessCenter.getInstance().receiveStringMsg(msg);
        }
    }

    public void distributeByteMessage(ByteMsg msg){
        if(UserStringType.contain(msg.getType())){
            MessageHub.getInstance().receiveByteMsg(msg);
        } else if (NormalStringType.contain(msg.getType())) {
            ;;;;
        }
    }
}
