package com.example.mychatapplication.repository.SDcardRepository;

import com.example.mychatapplication.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardDao {
    private final static String tag = "SDCardDao";
    private static SDCardDao sdCardDao;
    private SDCardDao(){}
    public synchronized static SDCardDao getInstance(){
        if(sdCardDao == null){
            sdCardDao = new SDCardDao();
        }
        return sdCardDao;
    }

    public boolean createFolder(File parent, String folderName){
        File folder = new File(parent, folderName);
        return folder.mkdir();
    }

    public void saveAvatar(String fileName, byte[] bytes) {
        if(!MainApplication.getInstance().avatarFolder.exists()){
            createFolder(MainApplication.getInstance().publicFile, "avatar");
        }
        File avatarFile = new File(MainApplication.getInstance().avatarFolder.getAbsoluteFile(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(avatarFile);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveCache(String fileName, byte[] bytes) {
        if(!MainApplication.getInstance().cacheFolder.exists()){
            createFolder(MainApplication.getInstance().publicFile, "cache");
        }
        File cacheFile = new File(MainApplication.getInstance().cacheFolder.getAbsoluteFile(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(cacheFile);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void saveChatImg(String chatId, String fileName, byte[] bytes) {
        if(!MainApplication.getInstance().chatFolder.exists()){
            createFolder(MainApplication.getInstance().publicFile, "chat");
        }
        if(!((new File(MainApplication.getInstance().chatFolder, chatId)).exists())){
            createFolder(MainApplication.getInstance().chatFolder, chatId);
        }
        File chatImgFile = new File(new File(MainApplication.getInstance().chatFolder, chatId), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(chatImgFile);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
