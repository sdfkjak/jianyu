package com.example.mychatapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mychatapplication.util.OkHttpUtil;
import com.example.mychatapplication.util.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisteredActivity extends AppCompatActivity implements View.OnClickListener{
    //http://172.19.50.90:8081/register
    private String regUrl = "http://172.19.50.90:8081/register";
    private String regMeg;
    private Bitmap avatar;
    private EditText et_nickname, et_password, et_phone;
    private TextView tv_nicknameMeg, tv_passwordMeg, tv_phoneMeg;
    private ImageView iv_avatar;
    private Button bt_register;
    private int accountMaxLength = 16, accountMinLength = 1;
    private int passwordMaxLength = 16, passwordMinLength = 6;
    private boolean isNicknameOK = false;
    private boolean isPhoneOK = false;
    private boolean isPasswordOK = false;
    private ActivityResultLauncher selectImgLaunch, cropImgLaunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        iv_avatar = findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(this);
        tv_nicknameMeg = findViewById(R.id.tv_nicknameMeg);
        et_nickname = findViewById(R.id.et_nickname);
        et_nickname.addTextChangedListener(new HideTextWatcher(et_nickname, accountMaxLength, accountMinLength));
        tv_passwordMeg = findViewById(R.id.tv_passwordMeg);
        et_password = findViewById(R.id.et_password);
        et_password.addTextChangedListener(new HideTextWatcher(et_password, passwordMaxLength, passwordMinLength));
        tv_phoneMeg = findViewById(R.id.tv_phoneMeg);
        et_phone = findViewById(R.id.et_phone);
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone, 11, 11));
        bt_register = findViewById(R.id.bt_register);
        bt_register.setOnClickListener(this);

        cropImgLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null && result.getResultCode() == RESULT_OK){
                    avatar = (Bitmap)result.getData().getExtras().get("data");
                    iv_avatar.setImageBitmap(avatar);
                }
            }
        });
        selectImgLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null && result.getResultCode() == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(result.getData().getData(), "image/*");
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    intent.putExtra("outputX", 256);
                    intent.putExtra("outputY", 256);
                    intent.putExtra("return-data", true);
                    cropImgLaunch.launch(intent);
                }
            }
        });
    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.bt_register){
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("nickname", et_nickname.getText().toString());
            builder.addFormDataPart("password", et_password.getText().toString());
            builder.addFormDataPart("phone", et_phone.getText().toString());
            if(avatar != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] avatarBytes = baos.toByteArray();
                builder.addFormDataPart("avatar", "avatar.png", RequestBody.create(avatarBytes, MediaType.parse("image/png")));
            }else{
                Bitmap defaultAvatar = BitmapFactory.decodeResource(RegisteredActivity.this.getResources(), R.drawable.defaultavater);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                defaultAvatar.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                builder.addFormDataPart("avatar", "avatar.png", RequestBody.create(byteArrayOutputStream.toByteArray(), MediaType.parse("image/png")));
            }

            RequestBody requestBody = builder.build();
            OkHttpUtil.getInstance().sendOkHttpPostRequest(MainApplication.regUrl,requestBody, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    regMeg = e.toString();
                    runOnUiThread(() -> Toast.makeText(RegisteredActivity.this, regMeg, Toast.LENGTH_LONG).show());
                }
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    regMeg = response.body().string();
                    runOnUiThread(() -> Toast.makeText(RegisteredActivity.this, regMeg, Toast.LENGTH_LONG).show());
                }
            });
        } else if (v.getId() == R.id.iv_avatar) {
            if(PermissionUtil.checkPermission(this, PermissionUtil.photoAlbumPermission, PermissionUtil.photoAlbumPermissionCode)){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                selectImgLaunch.launch(intent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtil.checkGrand(grantResults)){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            selectImgLaunch.launch(intent);
        }else{
            Toast.makeText(RegisteredActivity.this, "您已拒绝", Toast.LENGTH_LONG).show();
        }
    }

    //判断EditText内容及相应操作
    private class HideTextWatcher implements TextWatcher{
        private EditText mEditText;
        private int mMaxLength, mMinLength;
        public HideTextWatcher(EditText v, int maxLength, int minLength){
            super();
            mEditText = v;
            mMaxLength = maxLength;
            mMinLength = minLength;
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after){};
        public void onTextChanged(CharSequence s, int start, int before, int count){};
        public void afterTextChanged(Editable s){
            String str = s.toString().trim();
            switch(mEditText.getId()){
                case R.id.et_nickname:
                    if(str.length() < mMinLength){
                        tv_nicknameMeg.setText("昵称长度不得小于" + String.valueOf(mMinLength));
                    } else if (str.length() >= mMinLength && str.length() <= mMaxLength) {
                        tv_nicknameMeg.setText("OK");
                        if(str.length() == mMaxLength){
                            tv_nicknameMeg.setText("已达最大长度" + String.valueOf(mMaxLength));
                            InputMethodManager imm = (InputMethodManager) RegisteredActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                        isNicknameOK = true;
                        if(isNicknameOK && isPhoneOK && isPasswordOK){
                            bt_register.setClickable(true);
                            bt_register.setBackgroundResource(R.drawable.shape_register_button_clickable);
                            bt_register.setTextColor(getResources().getColor(R.color.white, null));
                        }
                    }else{
                        str = str.substring(0, mMaxLength);
                        Log.d("str", str);
                        mEditText.setText(str);
                        mEditText.setSelection(str.length());
                    }
                    break;
                case R.id.et_phone:
                    if(str.length() < mMinLength){
                        tv_phoneMeg.setText("手机号长度不得小于" + String.valueOf(mMinLength));
                    } else if (str.length() >= mMinLength && str.length() <= mMaxLength) {
                        tv_phoneMeg.setText("OK");
                        if(str.length() == mMaxLength){
                            tv_phoneMeg.setText("已达最大长度" + String.valueOf(mMaxLength));
                            InputMethodManager imm = (InputMethodManager) RegisteredActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                        isPhoneOK = true;
                        if(isNicknameOK && isPhoneOK && isPasswordOK){
                            bt_register.setClickable(true);
                            bt_register.setBackgroundResource(R.drawable.shape_register_button_clickable);
                            bt_register.setTextColor(getResources().getColor(R.color.white, null));
                        }
                    }else{
                        str = str.substring(0, mMaxLength);
                        Log.d("str", str);
                        mEditText.setText(str);
                        mEditText.setSelection(str.length());
                    }
                    break;
                case R.id.et_password:
                    if(str.length() < mMinLength){
                        tv_passwordMeg.setText("密码长度不得小于" + String.valueOf(mMinLength));
                    } else if (str.length() >= mMinLength && str.length() <= mMaxLength) {
                        tv_passwordMeg.setText("OK");
                        if(str.length() == mMaxLength){
                            tv_passwordMeg.setText("已达最大长度" + String.valueOf(mMaxLength));
                            InputMethodManager imm = (InputMethodManager) RegisteredActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                        isPasswordOK = true;
                        if(isNicknameOK && isPhoneOK && isPasswordOK){
                            bt_register.setClickable(true);
                            bt_register.setBackgroundResource(R.drawable.shape_register_button_clickable);
                            bt_register.setTextColor(getResources().getColor(R.color.white, null));
                        }
                    }else{
                        str = str.substring(0, mMaxLength);
                        Log.d("str", str);
                        mEditText.setText(str);
                        mEditText.setSelection(str.length());
                    }
                    break;
            }
        };
    }
}