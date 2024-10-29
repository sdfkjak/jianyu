package com.example.mychatapplication.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtil {
    public static int photoAlbumPermissionCode = 100;
    public static String[] photoAlbumPermission = {Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    public static boolean checkPermission(Context context, String permission, int requestCode){
        return checkPermission(context, new String[]{permission}, requestCode);
    }

    public static boolean checkPermission(Context context, String[] permissions, int requestCode){
        boolean result = true;
        int check = PackageManager.PERMISSION_GRANTED;
        for (String permission: permissions) {
            check = ContextCompat.checkSelfPermission(context, permission);
            if(check != PackageManager.PERMISSION_GRANTED){
                Log.d("未获取的权限", permission);
                break;
            }
        }
        if(check != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
            result = false;
        }
        return result;
    }

    public static boolean checkGrand(int[] grantResults){
        boolean result = true;
        if(grantResults != null){
            for (int grantResult: grantResults) {
                if(grantResult != PackageManager.PERMISSION_GRANTED){
                    result = false;
                }
            }
        }else{
            result = false;
        }
        return result;
    }
}
