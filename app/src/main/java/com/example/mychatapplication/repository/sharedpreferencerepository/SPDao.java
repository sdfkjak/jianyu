package com.example.mychatapplication.repository.sharedpreferencerepository;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mychatapplication.MainApplication;

import java.util.HashMap;

public class SPDao {
    private static SPDao SPDao;

    private SPDao() {
    }

    public synchronized static SPDao getInstance() {
        if (SPDao == null) {
            SPDao = new SPDao();
        }
        return SPDao;
    }

    private SharedPreferences sharedPreference() {
        return MainApplication.getInstance().getSharedPreferences("jianyu", Context.MODE_PRIVATE);
    }

    public boolean saveLoginMsg(boolean isLogin, String jyId) {
        SharedPreferences.Editor editor = sharedPreference().edit();
        editor.putBoolean("isLogin", isLogin);
        editor.putString("jyid", jyId);
        editor.apply();
        return true;
    }

    public HashMap<String, String> getLoginMsg() {
        HashMap<String, String> result = new HashMap<>();
        result.put("isLogin", String.valueOf(sharedPreference().getBoolean("isLogin", false)));
        result.put("jyid", sharedPreference().getString("jyid", ""));
        return result;
    }
}
