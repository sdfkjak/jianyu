package com.example.mychatapplication.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mychatapplication.ChatActivity;
import com.example.mychatapplication.ChatViewModel;
import com.example.mychatapplication.R;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.util.FileUtil;
import com.example.mychatapplication.util.ImageUtil;
import com.example.mychatapplication.util.TimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.PrivateChatViewHolder> {
    public enum ChatType{
        MYCHAT, OTHERCHAT, TIME;
    }
    private List<PrivateChatInfo> privateChatInfoList = new ArrayList<>();
    private String myAvatar;
    private Bitmap myAvatarBitmap;
    private String otherAvatar;
    private Bitmap otherAvatarBitmap;
    private String otherJyId;
    private Context context;
    private String friendChatId;
    private ChatViewModel chatViewModel;
    private List<ChatDetailItem> chatDetailItemArrayList = new ArrayList<>();

    public List<ChatDetailItem> getChatDetailItemArrayList() {
        return chatDetailItemArrayList;
    }

    public List<PrivateChatInfo> getPrivateChatInfoList() {
        return privateChatInfoList;
    }

    public void setOtherJyId(String otherJyId) {
        this.otherJyId = otherJyId;
    }

    public PrivateChatAdapter(Context context) {
        this.context = context;
        chatViewModel = new ChatViewModel((Application) context.getApplicationContext());
    }

    public void setFriendChatId(String friendChatId) {
        this.friendChatId = friendChatId;
    }

    public void setMyAvatar(String myAvatar) {
        this.myAvatar = myAvatar;
        this.myAvatarBitmap = ImageUtil.convertBase64ToBitmap(myAvatar);
    }

    public String getMyAvatar() {
        return myAvatar;
    }

    public void setOtherAvatar(String otherAvatar) {
        this.otherAvatar = otherAvatar;
        this.otherAvatarBitmap = ImageUtil.convertBase64ToBitmap(otherAvatar);
    }

    public void setPrivateChatInfoList(List<PrivateChatInfo> privateChatInfoList) {
        this.privateChatInfoList = privateChatInfoList;
        initChatDetailItemLinkedList();
    }

    private void initChatDetailItemLinkedList(){
        chatDetailItemArrayList.clear();
        if(privateChatInfoList != null){
            chatDetailItemArrayList.add(new ChatDetailItem(ChatType.TIME.toString(), privateChatInfoList.get(0).getTimestamp()));
            if(privateChatInfoList.get(0).getType().equals("TEXT")){
                chatDetailItemArrayList.add(new ChatDetailItem(privateChatInfoList.get(0).isMe()?ChatType.MYCHAT.toString():ChatType.OTHERCHAT.toString(), privateChatInfoList.get(0).getContent(), privateChatInfoList.get(0).getTimestamp()));
            } else if (privateChatInfoList.get(0).getType().equals("FRIENDCHATIMAGEMESSAGE")) {
                chatDetailItemArrayList.add(new ChatDetailItem(privateChatInfoList.get(0).isMe()?ChatType.MYCHAT.toString():ChatType.OTHERCHAT.toString(), privateChatInfoList.get(0).getTimestamp()));
            }
            for (int i = 0; i < privateChatInfoList.size(); i++) {
                if( i + 1 < privateChatInfoList.size()){
                    Log.d("选了几张", privateChatInfoList.get(i + 1).getType());
                    if(privateChatInfoList.get(i + 1).getTimestamp() - privateChatInfoList.get(i).getTimestamp() > 60 * 1000){
                        chatDetailItemArrayList.add(new ChatDetailItem(ChatType.TIME.toString(), privateChatInfoList.get(i + 1).getTimestamp()));
                    }
                    if(privateChatInfoList.get(i + 1).getType().equals("TEXT")){
                        chatDetailItemArrayList.add(new ChatDetailItem(privateChatInfoList.get(i + 1).isMe()?ChatType.MYCHAT.toString():ChatType.OTHERCHAT.toString(), privateChatInfoList.get(i + 1).getContent(), privateChatInfoList.get(i + 1).getTimestamp()));
                    } else if (privateChatInfoList.get(i + 1).getType().equals("FRIENDCHATIMAGEMESSAGE")) {
                        chatDetailItemArrayList.add(new ChatDetailItem(privateChatInfoList.get(i + 1).isMe()?ChatType.MYCHAT.toString():ChatType.OTHERCHAT.toString(), privateChatInfoList.get(i + 1).getTimestamp()));
                    }
                }
            }
        }

    }

    @NonNull
    @Override
    public PrivateChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case 0:
                view = layoutInflater.inflate(R.layout.me_chat, parent, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.other_chat, parent, false);
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.item_display_time, parent, false);
                break;
            default:
                view = null;
        }
        return new PrivateChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateChatViewHolder holder, int position) {
        //先隔1%
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        DisplayMetrics displayMetrics = holder.itemView.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int spacingY = (int) Math.round(height * 0.01);
        params.bottomMargin = spacingY;
        holder.itemView.setLayoutParams(params);
        ChatDetailItem chatDetailItem = chatDetailItemArrayList.get(position);
        switch (chatDetailItem.getType()){
            case "MYCHAT":
                Log.d("asdfasdfasdf", "wo");
                if(myAvatarBitmap == null){
                    chatViewModel.getCurrentUserInfoLiveData().observe((LifecycleOwner) context, new Observer<UserInfo>() {
                        @Override
                        public void onChanged(UserInfo userInfo) {
                            Glide.with(context).load(ImageUtil.convertBase64ToBitmap(userInfo.getAvatar())).into(holder.iv_avatar);
                            Log.d("图片长度", String.valueOf(myAvatarBitmap.toString().length()));
                        }
                    });
                }else{
                    Glide.with(context).load(myAvatarBitmap).into(holder.iv_avatar);
                    Log.d("图片长度", String.valueOf(myAvatarBitmap.toString().length()));
                }
//                holder.iv_avatar.setImageBitmap(myAvatarBitmap);
                if(chatDetailItem.getMessageType().equals("TEXT")){
                    Log.d("asdfasdfasdf", "TEXT");
                    holder.tv_content.setText(chatDetailItem.getContent());
                } else if (chatDetailItem.getMessageType().equals("FRIENDCHATIMAGEMESSAGE")) {
                    Log.d("asdfasdfasdf", "TEXFRIENDCHATIMAGEMESSAGET");
                    holder.tv_content.setVisibility(View.GONE);
                    holder.iv_message.setVisibility(View.VISIBLE);
                    String filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + friendChatId + "/" + chatDetailItem.timestamp + ".jpg";
                    new Thread(new readImageThread(filePath, displayMetrics, holder, position)).start();
                }
                break;
            case "OTHERCHAT":
                if(otherAvatarBitmap == null){
                    chatViewModel.getUserInfoLiveData(otherJyId).observe((LifecycleOwner) context, new Observer<UserInfo>() {
                        @Override
                        public void onChanged(UserInfo userInfo) {
                            Glide.with(context).load(ImageUtil.convertBase64ToBitmap(userInfo.getAvatar())).into(holder.iv_avatar);
                            Log.d("other图片长度", otherJyId);
                        }
                    });
                }else{
                    Glide.with(context).load(otherAvatarBitmap).into(holder.iv_avatar);
                    Log.d("other图片长度", otherJyId);
                }

                if(chatDetailItem.getMessageType().equals("TEXT")){
                    Log.d("asdfasdfasdf", "TEXT");
                    holder.tv_content.setText(chatDetailItem.getContent());
                } else if (chatDetailItem.getMessageType().equals("FRIENDCHATIMAGEMESSAGE")) {
                    Log.d("asdfasdfasdf", "TEXFRIENDCHATIMAGEMESSAGET");
                    holder.tv_content.setVisibility(View.GONE);
                    holder.iv_message.setVisibility(View.VISIBLE);
                    String filePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + "/" + friendChatId + "/" + chatDetailItem.timestamp + ".jpg";
                    new Thread(new readImageThread(filePath, displayMetrics, holder, position)).start();
//                    try {
//                        Bitmap originBitmap = FileUtil.readImageFile(filePath);
//                        int originBitmapWidth = originBitmap.getWidth();
//                        int originBitmapHeight = originBitmap.getHeight();
//                        int[] shape = ImageUtil.zoomChatPicture(displayMetrics, originBitmapHeight, originBitmapWidth);
//                        ViewGroup.LayoutParams iv_messageParams = holder.iv_message.getLayoutParams();
//                        iv_messageParams.height = shape[0];
//                        iv_messageParams.width = shape[1];
//                        holder.iv_message.setLayoutParams(iv_messageParams);
//                        // 加载为四个都是圆角的图片 可以设置圆角幅度
//                        RequestBuilder<Drawable> builder = Glide.with(context).load(originBitmap);
//                        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
//                        builder.apply(options).into(holder.iv_message);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
                }
                break;
            case "TIME":
                holder.tv_time.setText(TimeUtil.detailDateDisplayFormat(context, chatDetailItem.getTimestamp()));
                break;
            default:
                ;
        }
    }

    @Override
    public int getItemCount() {
        return chatDetailItemArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatDetailItemArrayList.get(position).getType()){
            case "MYCHAT":
                return 0;
            case "OTHERCHAT":
                return 1;
            case "TIME":
                return 2;
            default:
                return 100;
        }
    }

    static class PrivateChatViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_content, tv_time;
        private ImageView iv_avatar, iv_message;
        public PrivateChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_time = itemView.findViewById(R.id.tv_time);
            iv_message = itemView.findViewById(R.id.iv_message);
        }
    }
    public static class ChatDetailItem{
        private String type;
        private String messageType;
        private String content;
        private long timestamp;

        public String getType() {
            return type;
        }

        public String getContent() {
            return content;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getMessageType() {
            return messageType;
        }

        public ChatDetailItem(String type, String content, long timestamp) {
            this.type = type;
            this.messageType = "TEXT";
            this.content = content;
            this.timestamp = timestamp;
        }

        public ChatDetailItem(String type, long timestamp) {
            this.type = type;
            this.messageType = "FRIENDCHATIMAGEMESSAGE";
            this.timestamp = timestamp;
        }
    }

    private class readImageThread implements Runnable{
        private String path;
        private DisplayMetrics displayMetrics;
        private PrivateChatViewHolder holder;
        private int position;

        public readImageThread(String path, DisplayMetrics displayMetrics, PrivateChatViewHolder holder, int position) {
            this.path = path;
            this.displayMetrics = displayMetrics;
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void run() {
            Bitmap originBitmap = readImage(path, 3);
            int originBitmapWidth = originBitmap.getWidth();
            int originBitmapHeight = originBitmap.getHeight();
            int[] shape = ImageUtil.zoomChatPicture(displayMetrics, originBitmapHeight, originBitmapWidth);
            ((Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                ViewGroup.LayoutParams iv_messageParams = holder.iv_message.getLayoutParams();
                iv_messageParams.height = shape[0];
                iv_messageParams.width = shape[1];
                holder.iv_message.setLayoutParams(iv_messageParams);
                // 加载为四个都是圆角的图片 可以设置圆角幅度
                RequestBuilder<Drawable> builder = Glide.with(context).load(originBitmap);
                RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
                builder.apply(options).into(holder.iv_message);
            });
        }
    }
    private Bitmap readImage(String path, int times){
        if(times  == 0){
            throw new RuntimeException();
        }else{
            try {
                return FileUtil.readImageFile(path);
            } catch (FileNotFoundException e) {
                try {
                    Thread.sleep(500);
                    return readImage(path, times - 1);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }


    }
