package com.example.mychatapplication.util;

import android.util.Log;

import com.example.mychatapplication.MainApplication;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import okio.ByteString;

public class BuildMessageUtil {
    public static ByteString buildByteStringMessage(String type, String source, String target, String friendChatId, byte[] imgBytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] typeByte = (type != null ? type  + "|": "|").getBytes(StandardCharsets.UTF_8);
        byte[] sourceByte = (source != null ? source  + "|": "|").getBytes(StandardCharsets.UTF_8);
        byte[] targetByte = (target != null ? target  + "|": "|").getBytes(StandardCharsets.UTF_8);
        byte[] timestampByte = (System.currentTimeMillis() + "|").getBytes(StandardCharsets.UTF_8);
        byte[] friendChatIdByte = (friendChatId != null ? friendChatId  + "|": "|").getBytes(StandardCharsets.UTF_8);
        baos.write(typeByte);
        baos.write(sourceByte);
        baos.write(targetByte);
        baos.write(timestampByte);
        baos.write(friendChatIdByte);
        if(baos.size() <= 150){
            int paddingLength = 150 - baos.size();
            byte[] paddingBytes = new byte[paddingLength];
            Arrays.fill(paddingBytes, (byte)0);
            baos.write(paddingBytes);
        }else{
            Log.d("长度错误", baos.size() + "");
            throw new IOException();
        }
        baos.write(imgBytes);
        byte[] bytes = baos.toByteArray();
        return ByteString.of(bytes);
    }
}
