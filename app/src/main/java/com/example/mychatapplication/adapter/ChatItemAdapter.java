package com.example.mychatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.ChatActivity;
import com.example.mychatapplication.R;
import com.example.mychatapplication.commomclass.PrivateChat.FriendChatMessage;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.util.ImageUtil;
import com.example.mychatapplication.util.TimeUtil;
import com.google.gson.Gson;

import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ChatItemViewHolder> {
    private List<UserInfo> userInfoList;
    private Context context;

    public ChatItemAdapter(Context context) {
        this.context = context;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    @NonNull
    @Override
    public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_chat, parent, false);
        return new ChatItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemViewHolder holder, int position) {
        holder.itemView.setBackgroundResource(R.color.white);
        UserInfo userInfo = userInfoList.get(position);
        holder.iv_avatar.setImageBitmap(ImageUtil.convertBase64ToBitmap(userInfo.getAvatar()));
        holder.tv_nickname.setText(userInfo.getNickname());
        FriendChatMessage[] friendChatMessages = new Gson().fromJson(userInfo.getFriendChatMessages(), FriendChatMessage[].class);
        FriendChatMessage lastFriendChatMessages = friendChatMessages[friendChatMessages.length - 1];
        if(lastFriendChatMessages.getType().equals("TEXT")){
            holder.tv_message.setText(lastFriendChatMessages.getContent());
        } else if (lastFriendChatMessages.getType().equals("FRIENDCHATIMAGEMESSAGE")) {
            holder.tv_message.setText("[图片]");
        }

        holder.tv_time.setText(TimeUtil.dateDisplayFormat(context, lastFriendChatMessages.getTimestamp()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("jyId", userInfo.getJyId());
                v.getContext().startActivity(intent);
            }
        });
        if(userInfoList.size() == position + 1){
            holder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userInfoList.size();
    }

    class ChatItemViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_avatar;
        TextView tv_nickname, tv_message, tv_time;
        View divider;
        public ChatItemViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_time = itemView.findViewById(R.id.tv_time);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
