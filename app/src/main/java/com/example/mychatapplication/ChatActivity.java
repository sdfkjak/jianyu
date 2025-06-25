package com.example.mychatapplication;

import static com.example.mychatapplication.util.ToolbarUtil.setToolbarTitleCenter;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mychatapplication.adapter.MoreActionAdapter;
import com.example.mychatapplication.adapter.PrivateChatAdapter;
import com.example.mychatapplication.memory.ChatImageCacheManager;
import com.example.mychatapplication.model.ChatImage;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.model.FriendChat;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.model.sendWS.TimeStamp;
import com.example.mychatapplication.network.WebSocketService;
import com.example.mychatapplication.repository.SDcardRepository.SDCardRepository;
import com.example.mychatapplication.repository.sharedpreferencerepository.SPRepository;
import com.example.mychatapplication.util.ChatUtil;
import com.example.mychatapplication.util.PermissionUtil;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okio.ByteString;

public class ChatActivity extends BaseActivity {
    private final static String tag = "ChatActivity";
    private EditText et_message;
    private Button bt_send;
    private ImageView iv_moreAction, iv_expression, iv_voiceInput;
    private RecyclerView rv_chat, rv_moreActionContainer;
    private ConstraintLayout.LayoutParams rv_moreActionContainerParams;
    private ConstraintLayout.LayoutParams rv_chatParams;
    private PrivateChatAdapter privateChatAdapter;
    private MoreActionAdapter moreActionAdapter;
    private Toolbar tb_head;
    private String chatTargetJyId, chatTargetNickname, chatTargetAvatar;
    private User targetUser;
    private ChatViewModel chatViewModel;
    private boolean innerTrigger = false;
    private int windowHeight = 0;
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
    private boolean isFirstIn = true;
    private int oldChatMessageLength;
    private SPRepository spRepository;
    private int softKeyboardHeight;
    private ConstraintLayout CL_chat, constraintLayout;

    public ActivityResultLauncher getSelectPhotoAlbumLauncher() {
        return selectPhotoAlbumLauncher;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        CL_chat = findViewById(R.id.CL_Chat);
        constraintLayout = findViewById(R.id.constraintLayout);
        spRepository = SPRepository.getInstance();
        softKeyboardHeight = (int) spRepository.getSoftKeyboardHeight();
        ChatImageCacheManager.getInstance().flush();
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
        rv_moreActionContainerParams = (ConstraintLayout.LayoutParams) rv_moreActionContainer.getLayoutParams();
        rv_chat = findViewById(R.id.rc_chat);
        rv_chatParams = (ConstraintLayout.LayoutParams) rv_chat.getLayoutParams();
        et_message = findViewById(R.id.et_message);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        chatTargetJyId = getIntent().getExtras().getString("jyId");

        tb_head = findViewById(R.id.tb_head);
        setSupportActionBar(tb_head);
        setToolbarTitleCenter(tb_head);
        tb_head.setNavigationIcon(R.drawable.baseline_chevron_left_24);

//        chatViewModel = new ChatViewModel(getApplication());
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
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

        chatViewModel.getUserLiveData(chatTargetJyId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    //这个if就当初始化
                    if (targetUser == null) {
                        targetUser = user;
                        privateChatAdapter = new PrivateChatAdapter(ChatActivity.this, user);
                        tb_head.setTitle(user.getNickname());

                        rv_chat.setLayoutManager(new MyLinearLayout(ChatActivity.this));
                        rv_chat.setAdapter(privateChatAdapter);
                    }
                    if (user.getFriendChatMessage() != null) {
                        ChatMessage[] totalChatMessages = (new Gson()).fromJson(user.getFriendChatMessage(), ChatMessage[].class);
                        int addMessageLength = totalChatMessages.length - oldChatMessageLength;
                        if(addMessageLength > 0){
                            ChatMessage[] newChatMessages = new ChatMessage[addMessageLength];
                            System.arraycopy(totalChatMessages, oldChatMessageLength, newChatMessages, 0, totalChatMessages.length - oldChatMessageLength);
                            oldChatMessageLength = oldChatMessageLength + addMessageLength;
                            chatViewModel.addChatDetailItemList(Arrays.asList(newChatMessages));
                            privateChatAdapter.setChatDetailItemList(chatViewModel.getChatDetailItemList());
                            privateChatAdapter.notifyItemRangeInserted(chatViewModel.getChatDetailItemList().size() - 1, chatViewModel.getAddChatDetailItemCount());
//                        rv_chat.setItemViewCacheSize(privateChatAdapter.getChatMessageList().size());
                            rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemList().size() - 1);
                        }

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
                if (hasFocus && isMoreActionUp) {
                    rv_moreActionContainerAlphaAnim.reverse();
                }
            }
        });
        et_message.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
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
                            if (!isMoreActionUp && isKeyboardUp) {
                                setUpItem("all", false);
                            }
                        }
                    }
                    if(privateChatAdapter != null){
                        if (privateChatAdapter.getChatDetailItemList().size() - 1 > 0 && windowHeight != height) {
                            rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemList().size() - 1);
                            innerTrigger = true;
                        }
                        if (privateChatAdapter.getChatDetailItemList().size() - 1 > 0 && isFirstIn) {
                            rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemList().size() - 1);
                            isFirstIn = false;
                            innerTrigger = true;
                        }
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
                try {
                    sendMessage(message);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                long currentTimestamp = MainApplication.getInstance().getTimeStamp();
                FriendChat friendChat = new FriendChat(targetUser.getFriendChatId(), targetUser.getJyId(), new ChatMessage(MainApplication.getInstance().user.jyId, message, currentTimestamp));
                WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(friendChat));
                chatViewModel.insertSendMsgToDB(chatTargetJyId, new ChatMessage(MainApplication.getInstance().user.jyId, message, currentTimestamp));
            }
        });

        selectPhotoAlbumLauncher = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
            @Override
            public void onActivityResult(List<Uri> result) {
                long currentTimestamp = MainApplication.getInstance().getTimeStamp();
                for (Uri uri : result) {
                    Log.d(tag, uri.getScheme());
                    InputStream inputStream = null;
                    ByteArrayOutputStream byteArrayOutputStream;
                    byte[] imgByte;
                    int imgHeight;
                    int imgWidth;
                    try {
                        inputStream = ChatActivity.this.getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        inputStream = ChatActivity.this.getContentResolver().openInputStream(uri);
                        imgHeight = bitmap.getHeight();
                        imgWidth = bitmap.getWidth();
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1){
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        imgByte = byteArrayOutputStream.toByteArray();
                        ChatImageCacheManager.getInstance().addToCache(new ChatImage(MainApplication.getInstance().user.jyId, currentTimestamp, imgByte));
                        SDCardRepository.getInstance().saveChatImg(targetUser.getJyId(), currentTimestamp + "", imgByte);
                        chatViewModel.insertSendMsgToDB(targetUser.getJyId(), new ChatMessage(MainApplication.getInstance().user.jyId, imgWidth, imgHeight, currentTimestamp));
                        byte[] byteMsg = ChatUtil.buildByteMsg("FRIENDCHAT", targetUser.getJyId(), targetUser.getFriendChatId(), imgByte);
                        ByteString byteString = ByteString.of(byteMsg);
                        FriendChat friendChat = new FriendChat(targetUser.getFriendChatId(), targetUser.getJyId(), new ChatMessage(MainApplication.getInstance().user.jyId, imgWidth, imgHeight, currentTimestamp));
                        WebSocketService.getInstance().sendWSStringMsg(new Gson().toJson(friendChat));
                        WebSocketService.getInstance().sendWSByteStringMsg(byteString);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
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
        } else {
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
        if (PermissionUtil.checkGrand(grantResults)) {
            selectPhotoAlbumLauncher.launch("image/*");
        } else {
        }
    }

    private void setUpItem(String item, boolean isChoose) {
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

    private void sendMessage(String message) throws JSONException {
        et_message.setText("");
    }

    private void initMoreActionAnim() {
        moreActionValueAnimator = ValueAnimator.ofInt(0, softKeyboardHeight);
        moreActionValueAnimator.setDuration(200);
        moreActionValueAnimator.setInterpolator(new LinearInterpolator());
        moreActionValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                if (privateChatAdapter.getChatMessageList().size()> 0) {
                    rv_chat.scrollToPosition(privateChatAdapter.getChatDetailItemList().size() - 1);
                    innerTrigger = true;
                }
                rv_moreActionContainerParams.height = (int) animation.getAnimatedValue();
                rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);
                Log.d(tag, rv_chat.getHeight() + "");

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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(rv_chat.getHeight() > 1192 ){
                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(CL_chat);
                            constraintSet.clear(rv_chat.getId(), ConstraintSet.TOP);
                            constraintSet.connect(rv_chat.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.TOP);
                            constraintSet.applyTo(CL_chat);
                        }
                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {
            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
        closeMoreActionValueAnimator = ValueAnimator.ofInt(softKeyboardHeight, 0);
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
                if (isMoreActionUp) {
                    setSoftInputMode(true);
                    validCalculateVisibleHeight = false;
                    if (!et_message.hasFocus()) {
                        et_message.requestFocus();
                    }
                    inputMethodManager.showSoftInput(et_message, 0);
                } else {
                    setSoftInputMode(true);
                    rv_moreActionContainerParams.height = softKeyboardHeight;
                    rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);
                    validCalculateVisibleHeight = false;
                    closeKeyboard();
                }
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (isMoreActionUp) {
                    rv_moreActionContainerParams.height = 0;
                    validCalculateVisibleHeight = true;
                    setSoftInputMode(false);
                    rv_moreActionContainer.setLayoutParams(rv_moreActionContainerParams);
                    setUpItem("moreAction", false);
                    setUpItem("keyboard", true);
                } else {
                    setUpItem("moreAction", true);
                }
                isWaitThreadRunning = false;
                iv_moreAction.setClickable(true);
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

    private void setSoftInputMode(boolean isNothing) {
        if (isNothing) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            isKeyboardModeNothing = true;
        } else {
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

    class MyLinearLayout extends LinearLayoutManager {

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