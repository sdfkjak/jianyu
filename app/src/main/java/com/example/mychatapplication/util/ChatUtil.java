package com.example.mychatapplication.util;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ChatUtil {
    private final static int FIELD_SIZE = 30;
    static byte fillByte = 0x00;
    public static byte[] buildByteMsg(String type, String targetJyId, String targetFriendChatId, byte[] imgBytes) throws IOException {
        byte[] byteMsg = new byte[150 + imgBytes.length];
        byte[] typeByte = fillBytesToFieldSize(type, FIELD_SIZE, fillByte);
        byte[] sourceByte = fillBytesToFieldSize(MainApplication.getInstance().user.jyId, FIELD_SIZE, fillByte);
        byte[] targetByte = fillBytesToFieldSize(targetJyId, FIELD_SIZE, fillByte);
        byte[] timestampByte = fillBytesToFieldSize(MainApplication.getInstance().getTimeStamp() + "", FIELD_SIZE, fillByte);
        byte[] friendChatIdByte = fillBytesToFieldSize(targetFriendChatId, FIELD_SIZE, fillByte);
        System.arraycopy(typeByte, 0, byteMsg, 0, typeByte.length);
        System.arraycopy(sourceByte, 0, byteMsg, FIELD_SIZE, sourceByte.length);
        System.arraycopy(targetByte, 0, byteMsg, FIELD_SIZE * 2, targetByte.length);
        System.arraycopy(timestampByte, 0, byteMsg, FIELD_SIZE * 3, timestampByte.length);
        System.arraycopy(friendChatIdByte, 0, byteMsg, FIELD_SIZE * 4, friendChatIdByte.length);
        System.arraycopy(imgBytes, 0, byteMsg, FIELD_SIZE * 5, imgBytes.length);
        return byteMsg;
    }

    private static byte[] fillBytesToFieldSize(String input, int length, byte fillByte) {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        if (inputBytes.length == length) {
            return inputBytes;
        } else if (inputBytes.length < length) {
            byte[] result = new byte[length];
            System.arraycopy(inputBytes, 0, result, 0, inputBytes.length);
            for (int i = inputBytes.length; i < length; i++) {
                result[i] = fillByte;
            }
            return result;
        } else {
            // 如果输入字节长度大于所需长度，需要截断
            // 这里可以选择截断的方式，比如截取前length个字节，或者截取字符串的某部分再编码
            // 下面的例子是简单地截取前length个字节（可能包含不完整字符的编码）
            byte[] truncatedBytes = new byte[length];
            System.arraycopy(inputBytes, 0, truncatedBytes, 0, length);
            // 注意：这种截断可能会导致编码后的字符串无法正确解码回原始字符串
            // 在实际应用中，应该避免这种情况，或者采用更复杂的截断策略
            return truncatedBytes;
        }
    }
}
