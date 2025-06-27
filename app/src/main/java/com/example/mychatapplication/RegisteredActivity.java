package com.example.mychatapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mychatapplication.databinding.ActivityRegisteredBinding;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.example.mychatapplication.util.OkHttpUtil;
import com.example.mychatapplication.util.PermissionUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener{
    private String regMeg;
    private Bitmap avatar;
    private int accountMaxLength = 16, accountMinLength = 1;
    private int passwordMaxLength = 16, passwordMinLength = 6;
    private boolean isNicknameOK = false;
    private boolean isPhoneOK = false;
    private boolean isPasswordOK = false;
    private ActivityResultLauncher selectImgLaunch, cropImgLaunch;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    private SPRepository spRepository;
    private InputMethodManager imm;

    private ActivityRegisteredBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisteredBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        spRepository = SPRepository.getInstance();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        Rect rect = new Rect();
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                int height = rect.height();
                if(screenHeight != height){
                    spRepository.setSoftKeyboardHeight(screenHeight - height);
                }
            }
        };

        binding.ivAvatar.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);
        binding.regETNickname.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        binding.regETNickname.addTextChangedListener(new HideTextWatcher(binding.regETNickname, accountMaxLength, accountMinLength));
        binding.regETPassword.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        binding.regETPassword.addTextChangedListener(new HideTextWatcher(binding.regETPassword, passwordMaxLength, passwordMinLength));
        binding.regETPhone.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        binding.regETPhone.addTextChangedListener(new HideTextWatcher(binding.regETPhone, 11, 11));
        binding.btRegister.setOnClickListener(this);
        binding.btRegister.setClickable(false);
        cropImgLaunch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getData() != null && result.getResultCode() == RESULT_OK){
                    avatar = (Bitmap)result.getData().getExtras().get("data");
                    binding.ivAvatar.setImageBitmap(avatar);
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
            builder.addFormDataPart("nickname", binding.regETNickname.getText().toString());
            builder.addFormDataPart("password", binding.regETPassword.getText().toString());
            builder.addFormDataPart("phone", binding.regETPhone.getText().toString());
            if(avatar != null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                avatar.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] avatarBytes = baos.toByteArray();
                builder.addFormDataPart("avatar", "avatar.png", RequestBody.create(avatarBytes, MediaType.parse("image/png")));
            }else{
                Bitmap defaultAvatar = BitmapFactory.decodeResource(this.getResources(), R.drawable.defaultavater);
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
        } else if (v.getId() == R.id.iv_close) {
            finish();
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
                case R.id.regETNickname:
                    if(str.length() < mMinLength){
                        binding.tvNicknameMeg.setText("昵称长度不得小于" + mMinLength);
                    } else if (str.length() >= mMinLength && str.length() <= mMaxLength) {
                        binding.tvNicknameMeg.setText("OK");
                        if(str.length() == mMaxLength){
                            binding.tvNicknameMeg.setText("已达最大长度" + mMaxLength);
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                        isNicknameOK = true;
                        setRegisteredBtnClickable();
                    }else{
                        truncationEditText(str);
                    }
                    break;
                case R.id.regETPhone:
                    if(str.length() < mMinLength){
                        binding.tvPhoneMeg.setText("手机号长度不得小于" + mMinLength);
                    } else if (str.length() >= mMinLength && str.length() <= mMaxLength) {
                        binding.tvPhoneMeg.setText("OK");
                        if(str.length() == mMaxLength){
                            binding.tvPhoneMeg.setText("已达最大长度" + mMaxLength);
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                        isPhoneOK = true;
                        setRegisteredBtnClickable();
                    }else{
                        truncationEditText(str);
                    }
                    break;
                case R.id.regETPassword:
                    if(str.length() < mMinLength){
                        binding.tvPasswordMeg.setText("密码长度不得小于" + mMinLength);
                    } else if (str.length() >= mMinLength && str.length() <= mMaxLength) {
                        binding.tvPasswordMeg.setText("OK");
                        if(str.length() == mMaxLength){
                            binding.tvPasswordMeg.setText("已达最大长度" + mMaxLength);
                            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                        }
                        isPasswordOK = true;
                        setRegisteredBtnClickable();
                    }else{
                        truncationEditText(str);
                    }
                    break;
            }
        };

        private void truncationEditText(String str){
            str = str.substring(0, mMaxLength);
            mEditText.setText(str);
            mEditText.setSelection(str.length());
        }
    }

    private void setRegisteredBtnClickable(){
        if(isNicknameOK && isPhoneOK && isPasswordOK){
            binding.btRegister.setClickable(true);
            binding.btRegister.setBackgroundResource(R.drawable.shape_register_button_clickable);
            binding.btRegister.setTextColor(getResources().getColor(R.color.white, null));
        }else {
            binding.btRegister.setClickable(false);
        }
    }
}