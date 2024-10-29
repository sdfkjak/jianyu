package com.example.mychatapplication.adapter;

import android.graphics.Bitmap;

import com.example.mychatapplication.R;

public class PersonalInfo {
    private int itemName;
    private Bitmap img;
    private String itemInfo;
    private int QRCode;

    public int getQRCode() {
        return QRCode;
    }

    public void setQRCode(int QRCode) {
        this.QRCode = QRCode;
    }

    public PersonalInfo(int itemName) {
        this.itemName = itemName;
    }
    public PersonalInfo(int itemName, int QRCode) {
        this.itemName = itemName;
        this.QRCode = QRCode;
    }
    public PersonalInfo(int itemName, Bitmap img) {
        this.itemName = itemName;
        this.img = img;
    }

    public PersonalInfo(int itemName, String itemInfo) {
        this.itemName = itemName;
        this.itemInfo = itemInfo;
    }

    public int getItemName() {
        return itemName;
    }

    public void setItemName(int itemName) {
        this.itemName = itemName;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public static int[] getInfoNames() {
        return infoNames;
    }

    private static int[] infoNames= {R.string.avatar, R.string.name, R.string.pat, R.string.jyId, R.string.QRCode, R.string.moreInfo, R.string.ringtones, R.string.jyBean, R.string.myAddress};
}
