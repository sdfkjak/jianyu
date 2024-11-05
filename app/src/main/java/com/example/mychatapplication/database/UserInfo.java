package com.example.mychatapplication.database;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

//@Entity(indices = {@Index(value = "phone", unique = true)})
@Entity
public class UserInfo {
    @PrimaryKey()
    @NonNull
    @SerializedName("user_id")
    private String jyId;
    @SerializedName("user_nickname")
    private String nickname;
    @SerializedName("user_sex")
    private String sex;
    @SerializedName("user_avatar")
    private String avatar;
    private String friendChatMessages;
    @SerializedName("user_area")
    private String area;
    private String remark;
    private String friendChatId;

    public String getFriendChatMessages() {
        return friendChatMessages;
    }

    public void setFriendChatMessages(String friendChatMessages) {
        this.friendChatMessages = friendChatMessages;
    }

    public String getFriendChatId() {
        return friendChatId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getJyId() {
        return jyId;
    }

    public void setJyId(String jyId) {
        this.jyId = jyId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserInfo(@NonNull String jyId, String nickname, String sex, String avatar, String area, String friendChatId) {
        this.jyId = jyId;
        this.nickname = nickname;
        this.sex = sex;
        this.avatar = avatar;
        this.area = area;
        this.friendChatId = friendChatId;
    }
    @Ignore
    public UserInfo(@NonNull String jyId, String nickname, String sex, String area, String friendChatId) {
        this.jyId = jyId;
        this.nickname = nickname;
        this.sex = sex;
        this.area = area;
        this.friendChatId = friendChatId;
    }
    @Ignore
    public UserInfo(@NonNull String jyId, String avatar) {
        this.jyId = jyId;
        this.avatar = avatar;
    }

    public Map<String, String> notNoneFields(){
        Map<String, String> notNoneMap = new HashMap<>();
        Class<?> clazz = this.getClass();
        // 获取当前类及其父类的所有字段（包括私有字段）
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // 允许访问私有字段
            try {
                Object value = field.get(this); // 获取字段的值
                if (value != null) { // 检查值是否为空
                    if(!field.getName().equals("jyId")){
                        Log.d("不为空的属性名称", field.getName());
                        notNoneMap.put(field.getName(), value.toString());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return notNoneMap;
    }
}
