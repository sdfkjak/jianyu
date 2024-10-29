package com.example.mychatapplication.util;

import android.os.Environment;
import android.provider.MediaStore;

public class ExternalStorageUtil {
    public static boolean isExternalStorageWritable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static boolean isExternalStorageReadable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}
