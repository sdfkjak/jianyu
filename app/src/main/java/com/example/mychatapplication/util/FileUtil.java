package com.example.mychatapplication.util;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okio.ByteString;

public class FileUtil {
    public static void saveAvatar(String fileName, String destinationDirectory, byte[] imgByte) throws IOException {
        File saveFolder = new File(destinationDirectory);
        if(!saveFolder.exists()){
            if(saveFolder.mkdir()){
                Log.d("文件", "文件创建失败1");
            }
        }
        File outputFile = new File(destinationDirectory, fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(imgByte);
        fos.flush();
    }

    public static void saveImageFromByteString(String fileName, String destinationDirectory, byte[] imgByte) {

        File saveFolder = new File(destinationDirectory);
        if (!saveFolder.exists()) {
            if (saveFolder.mkdir()) {
                Log.d("文件", "文件创建失败1");
            }
        }

        // 创建输出文件
        File outputFile = new File(destinationDirectory, fileName);
        Log.d("文件存储路径", outputFile.getAbsolutePath());
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(imgByte); // 写入字节数组到文件
            fos.flush(); // 刷新输出流
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void saveChatImageFromUri(Context context, Uri uri, String fileName, String destinationDirectory) {
        ContentResolver contentResolver = context.getContentResolver();
        InputStream inputStream = null;
        FileOutputStream outputStream = null;

        File saveFolder = new File(destinationDirectory);
        if (!saveFolder.exists()) {
            if (saveFolder.mkdir()) {
                Log.d("文件", "文件创建失败1");
            }
        }

        try {
            // 获取图片的输入流
            inputStream = contentResolver.openInputStream(uri);

            // 创建输出文件
            File outputFile = new File(destinationDirectory, fileName);
            if (outputFile.createNewFile()) {
                Log.d("文件", "文件创建失败2");
            }
            Log.d("存储路径", outputFile.toString());
            // 写入输出文件
            outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // 刷新并关闭流
            outputStream.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 关闭流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap readImageFile(String path) throws FileNotFoundException {
        File imageFile = new File(path);
        Log.d("文件读取路径", imageFile.getAbsolutePath());
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        return BitmapFactory.decodeStream(fileInputStream);
    }
}
