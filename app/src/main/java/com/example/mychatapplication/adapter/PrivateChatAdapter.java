package com.example.mychatapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mychatapplication.ChatActivity;
import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.R;
import com.example.mychatapplication.memory.ChatImageCacheManager;
import com.example.mychatapplication.model.ChatDetailItem;
import com.example.mychatapplication.model.ChatImage;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.util.FileUtil;
import com.example.mychatapplication.util.ImageUtil;
import com.example.mychatapplication.util.TimeUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.PrivateChatViewHolder> {
    public final static String tag = "PrivateChatAdapter";
    private List<ChatDetailItem> chatDetailItemList = new ArrayList<>();
    private Context context;

    private User targetUser;
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public PrivateChatAdapter(Context context, User targetUser) {
        this.context = context;
        this.targetUser = targetUser;
    }

    public List<ChatDetailItem> getChatDetailItemList() {
        return chatDetailItemList;
    }

    public void setChatDetailItemList(List<ChatDetailItem> chatDetailItemList) {
        this.chatDetailItemList = chatDetailItemList;
    }

    @NonNull
    @Override
    public PrivateChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
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
        ChatDetailItem chatDetailItem = chatDetailItemList.get(position);
        Log.d(tag, "类型: " + chatDetailItem.getType() + " 消息类型: " + chatDetailItem.getMessageType());
        switch (chatDetailItem.getType()) {
            case "MYCHAT":
                RequestBuilder<Drawable> builder1 = Glide.with(context).load(new File(MainApplication.getInstance().avatarFolder, MainApplication.getInstance().user.jyId));
                builder1.into(holder.iv_avatar);
                if (chatDetailItem.getMessageType().equals("TEXT")) {
                    holder.tv_content.setVisibility(View.VISIBLE);
                    holder.iv_message.setVisibility(View.GONE);
                    holder.tv_content.setText(chatDetailItem.getContent());
                } else if (chatDetailItem.getMessageType().equals("IMAGE")) {
                    int[] shape = ImageUtil.zoomChatPicture(displayMetrics, chatDetailItem.getImgHeight(), chatDetailItem.getImgWidth());
                    ViewGroup.LayoutParams layoutParams = holder.iv_message.getLayoutParams();
                    layoutParams.width = shape[1];
                    layoutParams.height = shape[0];
                    holder.iv_message.setLayoutParams(layoutParams);
                    holder.tv_content.setVisibility(View.GONE);
                    holder.iv_message.setVisibility(View.VISIBLE);
                    holder.iv_message.setTag(chatDetailItem.getTimestamp());
                    ChatImageCacheManager.getInstance().getChatImageArrayListMutableLiveData().observe((LifecycleOwner) context, new Observer<ArrayList<ChatImage>>() {
                        @Override
                        public void onChanged(ArrayList<ChatImage> chatImages) {
                            if (chatImages.size() != 0) {
                                if(holder.iv_message.getTag().equals(chatDetailItem.getTimestamp())){
                                    boolean isExist = false;
                                    for (ChatImage chatImage : chatImages) {
                                        if (chatImage.getJyId().equals(MainApplication.getInstance().user.jyId) && chatImage.getTimestamp() == chatDetailItem.getTimestamp()) {
                                            Glide.with(context)
                                                    .load(chatImage.getImgByte())
                                                    .into(holder.iv_message);
                                            isExist = true;
                                            break;
                                        }
                                    }
                                    if (!isExist) {
                                        if (new File(new File(MainApplication.getInstance().chatFolder, targetUser.getJyId()), chatDetailItem.getTimestamp() + "").exists()) {
                                            Glide.with(context)
                                                    .load(new File(new File(MainApplication.getInstance().chatFolder, targetUser.getJyId()), chatDetailItem.getTimestamp() + ""))
                                                    .into(holder.iv_message);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
                break;
            case "OTHERCHAT":
                RequestBuilder<Drawable> builder2 = Glide.with(context).load(new File(MainApplication.getInstance().avatarFolder, targetUser.getJyId()));
                builder2.into(holder.iv_avatar);
                if (chatDetailItem.getMessageType().equals("TEXT")) {
                    holder.tv_content.setVisibility(View.VISIBLE);
                    holder.iv_message.setVisibility(View.GONE);
                    holder.tv_content.setText(chatDetailItem.getContent());
                } else if (chatDetailItem.getMessageType().equals("IMAGE")) {
                    int[] shape = ImageUtil.zoomChatPicture(displayMetrics, chatDetailItem.getImgHeight(), chatDetailItem.getImgWidth());
                    ViewGroup.LayoutParams layoutParams = holder.iv_message.getLayoutParams();
                    layoutParams.width = shape[1];
                    layoutParams.height = shape[0];
                    holder.iv_message.setLayoutParams(layoutParams);
                    holder.tv_content.setVisibility(View.GONE);
                    holder.iv_message.setVisibility(View.VISIBLE);
                    holder.iv_message.setTag(chatDetailItem.getTimestamp());
                    ChatImageCacheManager.getInstance().getChatImageArrayListMutableLiveData().observe((LifecycleOwner) context, new Observer<ArrayList<ChatImage>>() {
                        @Override
                        public void onChanged(ArrayList<ChatImage> chatImages) {
                            if (chatImages != null) {
                                if(holder.iv_message.getTag().equals(chatDetailItem.getTimestamp())){
                                    boolean isExist = false;
                                    for (ChatImage chatImage : chatImages) {
                                        if (chatImage.getJyId().equals(targetUser.getJyId()) && chatImage.getTimestamp() == chatDetailItem.getTimestamp()) {
                                            Glide.with(context)
                                                    .load(chatImage.getImgByte())
                                                    .into(holder.iv_message);
                                            isExist = true;
                                            break;
                                        }
                                    }
                                    if (!isExist) {
                                        if (new File(new File(MainApplication.getInstance().chatFolder, targetUser.getJyId()), chatDetailItem.getTimestamp() + "").exists()) {
                                            Glide.with(context)
                                                    .load(new File(new File(MainApplication.getInstance().chatFolder, targetUser.getJyId()), chatDetailItem.getTimestamp() + ""))
                                                    .into(holder.iv_message);

                                        }
                                    }
                                }
                            }
                        }
                    });

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
        return chatDetailItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatDetailItemList.get(position).getType()) {
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

    static class PrivateChatViewHolder extends RecyclerView.ViewHolder {
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
}
