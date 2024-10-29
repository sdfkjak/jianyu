package com.example.mychatapplication;

import static android.view.KeyEvent.KEYCODE_BACKSLASH;
import static com.example.mychatapplication.util.ToolbarUtil.setToolbarTitleCenter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mychatapplication.adapter.MoreActionAdapter;
import com.example.mychatapplication.adapter.PrivateChatAdapter;
import com.example.mychatapplication.adapter.PrivateChatInfo;
import com.example.mychatapplication.commomclass.PrivateChat.FriendChat;
import com.example.mychatapplication.commomclass.PrivateChat.FriendChatMessage;
import com.example.mychatapplication.commomclass.TalkMessage;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.util.ChatUtil;
import com.example.mychatapplication.util.FileUtil;
import com.example.mychatapplication.util.PermissionUtil;
import com.example.mychatapplication.util.ToolbarUtil;
import com.example.mychatapplication.widget.ExtendAnimation;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okio.ByteString;

public class ChatActivity extends AppCompatActivity {
    private EditText et_message;
    private Button bt_send;
    private ImageView iv_moreAction, iv_expression, iv_voiceInput;
    private RecyclerView rv_chat, rv_moreActionContainer;
    private ViewGroup.LayoutParams rv_moreActionContainerParams;
    private PrivateChatAdapter privateChatAdapter;
    private MoreActionAdapter moreActionAdapter;
    private Toolbar tb_head;
    private String chatTargetJyId, chatTargetNickname, chatTargetAvatar;
    private UserInfo targetUserInfo;
    private ChatViewModel chatViewModel;
    private boolean innerTrigger = false;
    private int windowHeight = 0;
    private int softKeyboardHeight;
    private InputMethodManager inputMethodManager;
    private boolean isKeyboardUp = false;
    private boolean isMoreActionUp = false;
    private boolean isMoreActionUpping = false;
    private boolean isMoreActionClosing = false;
    private AnimatorSet animSet;
    private boolean isMessageLastEmpty = true;
    private ValueAnimator moreActionValueAnimator;
    private ValueAnimator closeMoreActionValueAnimator;
    private ObjectAnimator rv_moreActionContainerAlphaAnim;
    private boolean isKeyboardModeNothing = false;
    private boolean validCalculateVisibleHeight = true;
    private boolean isWaitThreadRunning = false;
    private ActivityResultLauncher selectPhotoAlbumLauncher;
    private boolean firstIn;
    private int oldFriendChatMessagesLength = 0;
    private boolean isFirstIn = true;
    public ActivityResultLauncher getSelectPhotoAlbumLauncher() {
        return selectPhotoAlbumLauncher;
    }

    public RecyclerView getRv_chat() {
        return rv_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firstIn = true;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("isKeyboardUp")) {
                isKeyboardUp = savedInstanceState.getBoolean("isKeyboardUp");
            }
            if (savedInstanceState.containsKey("isMoreActionUp")) {
                isMoreActionUp = savedInstanceState.getBoolean("isMoreActionUp");

            }
        }
        initMoreActionAnim();
        rv_moreActionContainer = findViewById(R.id.rv_moreActionContainer);
        rv_moreActionContainer.setLayoutManager(new GridLayoutManager(this, 4));
        moreActionAdapter = new MoreActionAdapter(this);
        rv_moreActionContainer.setAdapter(moreActionAdapter);
        rv_moreActionContainerParams = rv_moreActionContainer.getLayoutParams();
        et_message = findViewById(R.id.et_message);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        privateChatAdapter = new PrivateChatAdapter(this);

        tb_head = findViewById(R.id.tb_head);
        setSupportActionBar(tb_head);

        setToolbarTitleCenter(tb_head);
        tb_head.setNavigationIcon(R.drawable.baseline_chevron_left_24);

        chatViewModel = new ChatViewModel(getApplication());
        chatViewModel.getCurrentUserInfoLiveData().observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                privateChatAdapter.setMyAvatar(userInfo.getAvatar());
            }
        });
        iv_moreAction = findViewById(R.id.iv_moreAction);
        bt_send = findViewById(R.id.bt_send);
        ViewGroup.LayoutParams bt_sendParams = bt_send.getLayoutParams();
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(bt_send, "alpha", 0f, 1f);
        ValueAnimator widthAnim = ValueAnimator.ofInt(80, 180);
        widthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                bt_sendParams.width = width;
                bt_send.setLayoutParams(bt_sendParams);
            }
        });
        animSet = new AnimatorSet();
        AnimatorSet.Builder builder = animSet.play(alphaAnim);
        builder.with(widthAnim);
        animSet.setDuration(200);
        animSet.setInterpolator(new LinearInterpolator());
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        chatTargetJyId = getIntent().getExtras().getString("jyId");
        chatViewModel.getUserInfo(chatTargetJyId).observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                if (userInfo != null) {
                    //这个if就当初始化
                    if(targetUserInfo == null){
                        Log.d("图片长度4", "chushihua1");
                        targetUserInfo = userInfo;
                        privateChatAdapter.setFriendChatId(userInfo.getFriendChatId());
                        privateChatAdapter.setOtherAvatar(userInfo.getAvatar());
                        privateChatAdapter.setOtherJyId(userInfo.getJyId());
                        tb_head.setTitle(userInfo.getNickname());
                        rv_chat = findViewById(R.id.rc_chat);
                        rv_chat.setLayoutManager(new MyLinearLayout(ChatActivity.this));
                        rv_chat.setAdapter(privateChatAdapter);
                    }
                    if (userInfo.getFriendChatMessages() != null) {
                        ArrayList<PrivateChatInfo> privateChatInfoArrayList = new ArrayList<>();
                        FriendChatMessage[] friendChatMessages = (new Gson()).fromJson(userInfo.getFriendChatMessages(), FriendChatMessage[].class);
                        int newFriendChatMessagesLength= friendChatMessages.length;
                        int upDateLength = newFriendChatMessagesLength - oldFriendChatMessagesLength;
                        if(upDateLength == 0){
                        } else if (upDateLength == 1) {
                            if(privateChatAdapter.getChatDetailItemArrayList().size() == 0){
                                privateChatAdapter.getChatDetailItemArrayList().add(new PrivateChatAdapter.ChatDetailItem(PrivateChatAdapter.ChatType.TIME.toString(), friendChatMessages[friendChatMessages.length - 1].getTimestamp()));
                                privateChatAdapter.notifyItemInserted(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                            }
                            for (int i = privateChatAdapter.getChatDetailItemArrayList().size() - 1 ; i > 0; i--) {
                                if(privateChatAdapter.getChatDetailItemArrayList().get(i).getType().equals("MYCHAT") || privateChatAdapter.getChatDetailItemArrayList().get(i).getType().equals("OTHERCHAT")){
                                    if(friendChatMessages[friendChatMessages.length - 1].getTimestamp() - privateChatAdapter.getChatDetailItemArrayList().get(i).getTimestamp() > 60 * 1000){
                                        privateChatAdapter.getChatDetailItemArrayList().add(new PrivateChatAdapter.ChatDetailItem(PrivateChatAdapter.ChatType.TIME.toString(), friendChatMessages[friendChatMessages.length - 1].getTimestamp()));
                                        privateChatAdapter.notifyItemInserted(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                                    }
                                    break;
                                }
                            }
                            if(friendChatMessages[friendChatMessages.length - 1].getType().equals("TEXT")){
                                privateChatAdapter.getChatDetailItemArrayList().add(new PrivateChatAdapter.ChatDetailItem(friendChatMessages[friendChatMessages.length - 1].getSource().equals(MainApplication.getInstance().user.jyId) ? "MYCHAT":"OTHERCHAT", friendChatMessages[friendChatMessages.length - 1].getContent(), friendChatMessages[friendChatMessages.length - 1].getTimestamp()));
                            }else if (friendChatMessages[friendChatMessages.length - 1].getType().equals("FRIENDCHATIMAGEMESSAGE")){
                                privateChatAdapter.getChatDetailItemArrayList().add(new PrivateChatAdapter.ChatDetailItem(friendChatMessages[friendChatMessages.length - 1].getSource().equals(MainApplication.getInstance().user.jyId) ? "MYCHAT":"OTHERCHAT", friendChatMessages[friendChatMessages.length - 1].getTimestamp()));
                            }
                            privateChatAdapter.notifyItemInserted(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                        }else{
                            for (FriendChatMessage friendChatMessage : friendChatMessages) {
                                if(friendChatMessage.getType().equals("TEXT")){
                                    privateChatInfoArrayList.add(new PrivateChatInfo(friendChatMessage.getSource().equals(MainApplication.getInstance().user.jyId), friendChatMessage.getTimestamp(), friendChatMessage.getContent()));
                                } else if (friendChatMessage.getType().equals("FRIENDCHATIMAGEMESSAGE")) {
                                    privateChatInfoArrayList.add(new PrivateChatInfo(friendChatMessage.getSource().equals(MainApplication.getInstance().user.jyId), friendChatMessage.getType(), friendChatMessage.getTimestamp()));
                                }
                            }
                            privateChatAdapter.setPrivateChatInfoList(privateChatInfoArrayList);
                            privateChatAdapter.notifyDataSetChanged();
                        }
                        rv_chat.setItemViewCacheSize(privateChatAdapter.getChatDetailItemArrayList().size());
                        rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                        oldFriendChatMessagesLength = newFriendChatMessagesLength;
                    }
                }
            }
        });
        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String message = editable.toString().trim();
                if (isMessageLastEmpty && !message.equals("")) {
                    isMessageLastEmpty = false;
                    bt_send.setVisibility(View.VISIBLE);
                    animSet.start();
                } else {
                    if (!isMessageLastEmpty && message.equals("")) {
                        isMessageLastEmpty = true;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            animSet.reverse();
                            bt_send.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });
        et_message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus && isMoreActionUp){
                    rv_moreActionContainerAlphaAnim.reverse();
                }
            }
        });
        et_message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("触发", "触发");
                if (!innerTrigger && !isMoreActionClosing && !isMoreActionUpping && !isWaitThreadRunning) {
                    Rect rect = new Rect();
                    getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    int height = rect.height();
                    if (windowHeight == 0) {
                        //初始状态
                        windowHeight = height;
                        setUpItem("keyboard", false);
                    } else {
                        if (windowHeight != height) {
                            //键盘已经弹出
                            softKeyboardHeight = windowHeight - height;
                            setUpItem("keyboard", true);
                        } else {
                            Log.d("kkkkkkkkcuowudian", String.valueOf(height));
                            if(!isMoreActionUp && isKeyboardUp){
                                setUpItem("all", false);
                            }
                        }
                    }
                    if (privateChatAdapter.getChatDetailItemArrayList().size() - 1 > 0 && windowHeight != height) {
                        Log.d("触发", "触发1");
                        rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                        innerTrigger = true;
                    }
                    if (privateChatAdapter.getChatDetailItemArrayList().size() - 1 > 0 && isFirstIn) {
                        Log.d("触发", "触发1");
                        rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                        isFirstIn = false;
                        innerTrigger = true;
                    }
                } else {
                    innerTrigger = false;
                }
            }
        });
        iv_moreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMoreActionView();
            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_message.getText().toString();
                long sendTimestamp = System.currentTimeMillis();
                try {
                    sendMessage(message, sendTimestamp);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                WebSocketClass.getInstance().getWebSocket().send((new Gson()).toJson(new FriendChat(targetUserInfo.getFriendChatId(), targetUserInfo.getJyId(), new FriendChatMessage(message, sendTimestamp))));
            }
        });
        selectPhotoAlbumLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
            @Override
            public void onActivityResult(List<Uri> result) {
                for (Uri uri: result) {
                    long timestamp = System.currentTimeMillis();
                    String fileName = timestamp + ".jpg";
                    String destinationDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + targetUserInfo.getFriendChatId();
                    FileUtil.saveChatImageFromUri(ChatActivity.this, uri, fileName, destinationDirectory);
                    try {
                        ChatUtil.addFriendChatMessages(targetUserInfo.getJyId(), new JSONArray().put(new JSONObject((new Gson()).toJson(new FriendChatMessage(MainApplication.getInstance().user.jyId, "FRIENDCHATIMAGEMESSAGE", timestamp)))));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] typeByte = "FRIENDCHAT|".getBytes(StandardCharsets.UTF_8);
                        byte[] sourceByte = (MainApplication.getInstance().user.jyId + "|").getBytes(StandardCharsets.UTF_8);
                        byte[] targetByte = (targetUserInfo.getJyId() + "|").getBytes(StandardCharsets.UTF_8);
                        byte[] timestampByte = (System.currentTimeMillis() + "|").getBytes(StandardCharsets.UTF_8);
                        byte[] friendChatIdByte = (targetUserInfo.getFriendChatId() + "|").getBytes(StandardCharsets.UTF_8);
                        baos.write(typeByte);
                        baos.write(sourceByte);
                        baos.write(targetByte);
                        baos.write(timestampByte);
                        baos.write(friendChatIdByte);
                        if(baos.size() <= 150){
                            int paddingLength = 150 - baos.size();
                            byte[] paddingBytes = new byte[paddingLength];
                            Arrays.fill(paddingBytes, (byte)0);
                            baos.write(paddingBytes);
                        }else{
                            Log.d("长度错误", baos.size() + "");
                            throw new IOException();
                        }
                        Log.d("长度错误", String.valueOf(baos.size()));
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        FileInputStream fileInputStream = new FileInputStream(destinationDirectory + "/" + fileName);
                        while((bytesRead = fileInputStream.read(buffer)) != -1){
                            baos.write(buffer, 0, bytesRead);
                        }
                        byte[] bytes = baos.toByteArray();
                        Log.d("长度错误", String.valueOf(baos.size()));
                        ByteString byteString = ByteString.of(bytes);
                        WebSocketClass.getInstance().getWebSocket().send(byteString);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

                Log.d("选了几张", String.valueOf(result.size()));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isKeyboardUp) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            inputMethodManager.showSoftInput(et_message, 0);
            setUpItem("keyboard", true);
        } else if (isMoreActionUp) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            setUpItem("moreAction", true);
            Log.d("这里", "trigger");
        }else{
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isKeyboardUp) {
            outState.putBoolean("isKeyboardUp", true);
        } else if (isMoreActionUp) {
            outState.putBoolean("isMoreActionUp", true);

        } else {
            outState.putBoolean("isKeyboardUp", false);
            outState.putBoolean("isMoreActionUp", false);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("kkkkkkkkkkkkkkkk", String.valueOf(isMoreActionUp) + String.valueOf(isKeyboardUp));
        if (isMoreActionUp || isKeyboardUp) {
            closeMoreAction();
            setSoftInputMode(false);
            setUpItem("all", false);
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionUtil.checkGrand(grantResults)){
            selectPhotoAlbumLauncher.launch("image/*");
            Log.d("权限2", "success");
        }else{
            Log.d("权限2", "false");
        }
    }

    private void setUpItem(String item, boolean isChoose) {
        Log.d("kkkkkkkk", String.valueOf(isKeyboardUp) + String.valueOf(isMoreActionUp) + item + String.valueOf(isChoose));
        if (item.equals("keyboard")) {
            if (isChoose) {
                isKeyboardUp = true;
                isMoreActionUp = false;
            } else {
                isKeyboardUp = false;
            }
        } else if (item.equals("moreAction")) {
            if (isChoose) {
                isKeyboardUp = false;
                isMoreActionUp = true;
            } else {
                isKeyboardUp = true;
                isMoreActionUp = false;
            }
        } else {
            isKeyboardUp = false;
            isMoreActionUp = false;
        }
    }

    private void sendMessage(String message, long sendTimestamp) throws JSONException {
        et_message.setText("");
        ChatUtil.addFriendChatMessages(targetUserInfo.getJyId(), new JSONArray().put(new JSONObject((new Gson()).toJson(new FriendChatMessage(message, sendTimestamp)))));
    }

    private void initMoreActionAnim() {
        moreActionValueAnimator = ValueAnimator.ofInt(0, 835);
        moreActionValueAnimator.setDuration(200);
        moreActionValueAnimator.setInterpolator(new LinearInterpolator());
        moreActionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                if (privateChatAdapter.getChatDetailItemArrayList().size() - 1 > 0) {
                    rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemArrayList().size() - 1);
                    innerTrigger = true;
                }
                rv_moreActionContainerParams.height = (int) animation.getAnimatedValue();
                rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);

            }
        });
        moreActionValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                isMoreActionUpping = true;
                iv_moreAction.setClickable(false);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                isMoreActionUpping = false;
                iv_moreAction.setClickable(true);

                setSoftInputMode(false);
                setUpItem("moreAction", true);
                Log.d("sssssssssssssssss后", String.valueOf(isKeyboardUp) + String.valueOf(isMoreActionUp));
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });

        closeMoreActionValueAnimator = ValueAnimator.ofInt(835, 0);
        closeMoreActionValueAnimator.setDuration(200);
        closeMoreActionValueAnimator.setInterpolator(new LinearInterpolator());
        closeMoreActionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                rv_moreActionContainerParams.height = (int) animation.getAnimatedValue();
                rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);
            }
        });
        closeMoreActionValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                isMoreActionClosing = true;
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                isMoreActionClosing = false;
                setUpItem("moreAction", false);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });

        rv_moreActionContainerAlphaAnim = ObjectAnimator.ofFloat(rv_moreActionContainer, "alpha", 1f, 0f);
        rv_moreActionContainerAlphaAnim.setDuration(200);
        rv_moreActionContainerAlphaAnim.setInterpolator(new LinearInterpolator());
        rv_moreActionContainerAlphaAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {
                isWaitThreadRunning = true;
                iv_moreAction.setClickable(false);
                if(isMoreActionUp){
                    setSoftInputMode(true);
                    validCalculateVisibleHeight = false;
                    if(!et_message.hasFocus()){
                        et_message.requestFocus();
                    }
                    inputMethodManager.showSoftInput(et_message, 0);
                }else{
                    setSoftInputMode(true);
                    rv_moreActionContainerParams.height = 835;
                    rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);
                    validCalculateVisibleHeight = false;
                    closeKeyboard();
                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if(isMoreActionUp){
                    rv_moreActionContainerParams.height = 0;
                    validCalculateVisibleHeight = true;
                    setSoftInputMode(false);
                    rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);
                    setUpItem("moreAction", false);
                    setUpItem("keyboard", true);
                }else{
                    setUpItem("moreAction", true);
                }
                isWaitThreadRunning = false;
                iv_moreAction.setClickable(true);
                Log.d("sssssssssssssssss后", String.valueOf(isKeyboardUp) + String.valueOf(isMoreActionUp));
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }

    private void openMoreActionView() {
        Log.d("sssssssssssssssss前", String.valueOf(isKeyboardUp) + String.valueOf(isMoreActionUp));
        if (!isKeyboardUp && !isMoreActionUp) {
            //两个都没弹起来
            moreActionValueAnimator.start();
        } else if (isKeyboardUp && !isMoreActionUp) {
            //键盘弹起时
            rv_moreActionContainerAlphaAnim.start();
        } else if (!isKeyboardUp && isMoreActionUp) {
            //更多弹起时
            rv_moreActionContainerAlphaAnim.reverse();
        }

    }

    private void setSoftInputMode(boolean isNothing){
        if(isNothing){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            isKeyboardModeNothing = true;
        }else{
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            isKeyboardModeNothing = false;
        }

    }
    private void closeMoreAction() {
        closeMoreActionValueAnimator.start();
    }

    private void closeKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(et_message.getWindowToken(), 0);
        setUpItem("keyboard", false);
    }
    class MyLinearLayout extends LinearLayoutManager{

        public MyLinearLayout(Context context) {
            super(context);
        }

        public MyLinearLayout(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void calculateExtraLayoutSpace(@NonNull RecyclerView.State state, @NonNull int[] extraLayoutSpace) {
            super.calculateExtraLayoutSpace(state, extraLayoutSpace);
            int position = 0;
            Log.d("位置a", String.valueOf(extraLayoutSpace.length));
            for (int i = 0; i < extraLayoutSpace.length; i++) {
                extraLayoutSpace[i] = 500;
            }
        }

//        @Override
//        protected int getExtraLayoutSpace(RecyclerView.State state) {
//            return super.getExtraLayoutSpace(state);
//
//        }
    }
}