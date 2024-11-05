package com.example.mychatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mychatapplication.ChatActivity;
import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.R;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.util.TimeUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.ChatItemViewHolder> {
    private List<User> userList;
    private Context context;

    public ChatItemAdapter(Context context) {
        this.context = context;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
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
        User user = userList.get(position);
        RequestBuilder<Drawable> builder = Glide.with(context).load(new File(MainApplication.getInstance().avatarFolder, user.getJyId()));
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
        builder.apply(options).into(holder.iv_avatar);
        holder.tv_nickname.setText(user.getNickname());
        ChatMessage[] chatMessages = new Gson().fromJson(user.getFriendChatMessage(), ChatMessage[].class);
        ChatMessage lastChatMessages = chatMessages[chatMessages.length - 1];
        if(lastChatMessages.getType().equals("TEXT")){
            holder.tv_message.setText(lastChatMessages.getContent());
        } else if (lastChatMessages.getType().equals("IMAGE")) {
            holder.tv_message.setText("[图片]");
        }

        holder.tv_time.setText(TimeUtil.dateDisplayFormat(context, lastChatMessages.getTimestamp()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("jyId", user.getJyId());
                v.getContext().startActivity(intent);
            }
        });
        if(userList.size() == position + 1){
            holder.divider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
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
