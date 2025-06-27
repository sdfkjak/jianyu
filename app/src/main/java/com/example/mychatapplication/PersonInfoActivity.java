package com.example.mychatapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mychatapplication.adapter.PersonalInfo;
import com.example.mychatapplication.adapter.PersonalInfoAdapter;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PersonInfoActivity extends BaseActivity {
    /**
     * 外部存储权限请求码
     */
    public static final int REQUEST_EXTERNAL_STORAGE_CODE = 9527;

    private PersonalInfoViewModel personalInfoViewModel;
    private RecyclerView rv_infoItems;
    private ImageView iv_back;
    private PersonalInfoAdapter personalInfoAdapter;
    private List<PersonalInfo> personalInfoList = new ArrayList<PersonalInfo>();
    private ActivityResultLauncher selectImageLauncher;
    private ActivityResultLauncher cropImageLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        rv_infoItems = findViewById(R.id.rv_infoItems);
        personalInfoAdapter = new PersonalInfoAdapter(this);
        rv_infoItems.setLayoutManager(new LinearLayoutManager(this));
        rv_infoItems.setAdapter(personalInfoAdapter);
        personalInfoViewModel = new ViewModelProvider(this).get(PersonalInfoViewModel.class);
        personalInfoViewModel.getUserLiveData(MainApplication.getInstance().user.getJyId()).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    personalInfoList.clear();
//                    oldUserInfo = user;
                    for(int i = 0; i < PersonalInfo.getInfoNames().length; i++){
                        if(getResources().getString(PersonalInfo.getInfoNames()[i]).equals("头像")){
                            personalInfoList.add(new PersonalInfo(PersonalInfo.getInfoNames()[i], "ii"));
                        }else if(getResources().getString(PersonalInfo.getInfoNames()[i]).equals("名字")){
                            personalInfoList.add(new PersonalInfo(PersonalInfo.getInfoNames()[i], user.getNickname()));
                        }else if(getResources().getString(PersonalInfo.getInfoNames()[i]).equals("微信号")){
                            personalInfoList.add(new PersonalInfo(PersonalInfo.getInfoNames()[i], user.getJyId()));
                        }else if(getResources().getString(PersonalInfo.getInfoNames()[i]).equals("二维码名片")){
                            personalInfoList.add(new PersonalInfo(PersonalInfo.getInfoNames()[i], R.drawable.baseline_qr_code_24));
                        }else{
                            personalInfoList.add(new PersonalInfo(PersonalInfo.getInfoNames()[i]));
                        }
                    }
                    personalInfoAdapter.setPersonalInfoList(personalInfoList);
                    personalInfoAdapter.notifyDataSetChanged();
                }
            }
        });
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cropImageLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Bitmap bitmap = (Bitmap)result.getData().getExtras().get("data");
                    String img = ImageUtil.convertBitmapToBase64(bitmap);
                    byte[] imgBytes = ImageUtil.bitmapToByteArray(bitmap);
                    personalInfoViewModel.saveAvatar(MainApplication.getInstance().user.getJyId(), imgBytes);
//                    try {
//                        WebSocketClass.getInstance().getWebSocket().send(BuildMessageUtil.buildByteStringMessage("MODIFYPERSONALINFO", MainApplication.getInstance().user.jyId, null, null, imgBytes));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    WebSocketClass.getInstance().getWebSocket().send(new Gson().toJson(new ModifyPersonalInfo("avatar", img)));
//                    UserInfo userInfo = oldUserInfo;
//                    userInfo.setAvatar(img);
//                    personalInfoViewModel.updateUserInfo(userInfo);
                }
            }
        });

        selectImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null){
                    if(result.getResultCode() == RESULT_OK){
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(result.getData().getData(), "image/*");
                        intent.putExtra("crop", "true");
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("outputX", 256);
                        intent.putExtra("outputY", 256);
                        intent.putExtra("return-data", true);
                        cropImageLaunch.launch(intent);
                    }
                }
            }
        });
    }
    /**
     * 权限请求结果
     * @param requestCode 请求码
     * @param permissions 请求权限
     * @param grantResults 授权结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(REQUEST_EXTERNAL_STORAGE_CODE)
    public void requestPermission(){
        String[] param = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this,param)){
            //已有权限
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            selectImageLauncher.launch(intent);
//            startActivityForResult(intent, PersonalInfoAdapter.REQUEST_CODE_PICK_IMAGE);
        }else {
            //无权限 则进行权限请求
            EasyPermissions.requestPermissions(this,"请求权限",REQUEST_EXTERNAL_STORAGE_CODE,param);
        }
    }
    private void showMsg(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}