package com.example.mychatapplication.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ImageUtil {
    public static String convertDrawableToBase64(Resources resources, int drawableId) {
        // 从drawable资源中加载图片为Bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(resources, drawableId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // 将字节数组编码为Base64字符串
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // 将字节数组编码为Base64字符串
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static Bitmap convertBase64ToBitmap(String base64String) {
        if (base64String == null) {
            return null;
        }
        // 去除Base64字符串中可能存在的数据URI前缀（如果有的话）
        if (base64String.startsWith("data:image/png;base64,")) {
            base64String = base64String.substring("data:image/png;base64,".length());
        } else if (base64String.startsWith("data:image/jpeg;base64,")) {
            base64String = base64String.substring("data:image/jpeg;base64,".length());
        }

        // 解码Base64字符串为字节数组
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);

        // 将字节数组转换为Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static Bitmap convertDoubleBase64ToBitmap(String doubleBase64String) {
        if (doubleBase64String == null) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            byte[] bytes = decoder.decode(doubleBase64String);
            doubleBase64String = new String(bytes, StandardCharsets.UTF_8);
        } else {
            doubleBase64String = null;
        }
        // 去除Base64字符串中可能存在的数据URI前缀（如果有的话）
        if (doubleBase64String.startsWith("data:image/png;base64,")) {
            doubleBase64String = doubleBase64String.substring("data:image/png;base64,".length());
        } else if (doubleBase64String.startsWith("data:image/jpeg;base64,")) {
            doubleBase64String = doubleBase64String.substring("data:image/jpeg;base64,".length());
        }

        // 解码Base64字符串为字节数组
        byte[] decodedBytes = Base64.decode(doubleBase64String, Base64.DEFAULT);

        // 将字节数组转换为Bitmap
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); // 使用PNG格式，质量100
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap setImgSize(Bitmap bm, int scale) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float k = ((float) scale) / width;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(k, k);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public static int[] zoomChatPicture(DisplayMetrics displayMetrics, int imageHeight, int imageWidth) {
        int screenHeight = displayMetrics.heightPixels;
        int screenWidth = displayMetrics.widthPixels;
        Log.d("daxiao1", "imageHeight:" + String.valueOf(imageHeight) + " imageWidth:" + String.valueOf(imageWidth));
        return zoomSmall(3, imageHeight, imageWidth, screenHeight, screenWidth);
    }

    private static int[] zoomSmall(int times, int actualHeight, int actualWidth, int screenHeight, int screenWidth) {
        if (actualHeight < screenHeight / 2 && actualWidth < screenWidth / 2) {
            return new int[]{actualHeight, actualWidth};
        }
        if (times >= 6) {
            return new int[]{(int) ((float) actualHeight / 2f), (int) ((float) actualWidth / 2f)};
        } else {
            actualHeight = (int) ((float) actualHeight * ((float) times / 6f));
            actualWidth = (int) ((float) actualWidth * ((float) times / 6f));
            return zoomSmall(times + 1, actualHeight, actualWidth, screenHeight, screenWidth);
        }
    }
}
