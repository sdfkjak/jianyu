package com.example.mychatapplication.repository.SDcardRepository;

import java.io.File;

public class SDCardRepository {
    private static SDCardRepository sdCardRepository;
    private SDCardDao sdCardDao;
    private SDCardRepository(){
        sdCardDao = SDCardDao.getInstance();
    };
    public synchronized static SDCardRepository getInstance(){
        if(sdCardRepository == null){
            sdCardRepository = new SDCardRepository();
        }
        return  sdCardRepository;
    }

    public void saveAvatar(String fileName, byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sdCardDao.saveAvatar(fileName, bytes);
            }
        }).start();
    }

    public void saveCache(String fileName, byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sdCardDao.saveCache(fileName, bytes);
            }
        }).start();
    }

    public void saveChatImg(String chatId, String fileName, byte[] bytes) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sdCardDao.saveChatImg(chatId, fileName, bytes);
            }
        }).start();
    }
    public boolean createFolder(File parent, String folderName){
        return sdCardDao.createFolder(parent, folderName);
    }
}
