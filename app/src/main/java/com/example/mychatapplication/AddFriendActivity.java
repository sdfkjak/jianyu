package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mychatapplication.commomclass.friendapplication.CommonUserInfo;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.util.ImageUtil;
import com.example.mychatapplication.websocketsend.QueryUserClass;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okio.ByteString;

public class AddFriendActivity extends AppCompatActivity{
    private EditText et_search;
    private Button bt_cancel;
    private ConstraintLayout cl_result;
    private TextView tv_search;
    private String searchString;
    private CommonUserInfo queryUserInfo;
    private byte[] avatarBytes;
    private int isOver = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        et_search = findViewById(R.id.et_search);
        bt_cancel = findViewById(R.id.bt_cancel);
        cl_result = findViewById(R.id.cl_result);
        cl_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchString != null){
                    Log.d("搜索", searchString);
                    Log.d("搜索", new Gson().toJson(new QueryUserClass(searchString)));
                    WebSocketClass.getInstance().getWebSocket().send(new Gson().toJson(new QueryUserClass(searchString)));
                }
            }
        });
        WebSocketClass.getInstance().registerListener(new WebSocketClass.MyWebSocketListener() {
            @Override
            public void onMessageReceived(JSONObject jsonObject) throws JSONException {
                if(jsonObject.getString("type").equals("QUERYUSERRESULT")){
                    Log.d("搜索", jsonObject.getJSONObject("userInfo").toString());
                    queryUserInfo = new Gson().fromJson(jsonObject.getJSONObject("userInfo").toString(), CommonUserInfo.class);
                    isOver = isOver + 1;
                    Log.d("搜索1", String.valueOf(isOver));
                    if(isOver == 2){
                        isOver = 0;
                        queryUserInfo.setAvatarBytes(avatarBytes);
                        startQueryUserInfoActivity();
                    }
                }

            }

            @Override
            public void onMessageReceived(String typeString, String sourceString, String timestampString, byte[] imgBytes) {
                if(typeString.equals("QUERYUSERRESULT") && sourceString.equals(MainApplication.getInstance().user.jyId)){
                    avatarBytes = imgBytes;
                    isOver = isOver + 1;
                    Log.d("搜索2", String.valueOf(isOver));
                    if(isOver == 2){
                        isOver = 0;
                        queryUserInfo.setAvatarBytes(avatarBytes);
                        startQueryUserInfoActivity();
                    }
                }

            }

            @Override
            public void onMessageReceived(int code, String reason) {

            }


        });
        tv_search = findViewById(R.id.tv_search);
        et_search.requestFocus();
        imm.showSoftInput(et_search, 0);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchString = s.toString();
                if(!searchString.trim().equals("")){
                    cl_result.setVisibility(View.VISIBLE);
                    tv_search.setText(searchString);
                }else{
                    cl_result.setVisibility(View.GONE);
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startQueryUserInfoActivity(){
        Intent intent = new Intent(this, QueryUserDetailActivity.class);
        intent.putExtra("userInfo", queryUserInfo);
        startActivity(intent);
    }
}