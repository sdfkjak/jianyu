package com.example.mychatapplication.adapter;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.R;
import com.example.mychatapplication.WebSocketClass;
import com.example.mychatapplication.commomclass.friendapplication.ReceiveFriendRequest;
import com.example.mychatapplication.commomclass.friendapplication.SendAcceptRequest;
import com.example.mychatapplication.util.ImageUtil;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {
    private ArrayList<ReceiveFriendRequest> receiveFriendRequestList;

    public void setReceiveFriendRequestList(ArrayList<ReceiveFriendRequest> receiveFriendRequestList) {
        this.receiveFriendRequestList = receiveFriendRequestList;
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ReceiveFriendRequest receiveFriendRequest = receiveFriendRequestList.get(position);
        if(receiveFriendRequest.getCommonUserInfo().getAvatarBytes() != null){
            holder.iv_avatar.setImageBitmap(BitmapFactory.decodeByteArray(receiveFriendRequest.getCommonUserInfo().getAvatarBytes(), 0, receiveFriendRequest.getCommonUserInfo().getAvatarBytes().length));
        }
        if(receiveFriendRequest.getMode().equals("passive")){
            holder.iv_toward.setVisibility(View.INVISIBLE);
        }else{
            holder.iv_toward.setVisibility(View.VISIBLE);
        }
        if(receiveFriendRequest.getFriendRequestMessageArrayList() != null){
            holder.tv_applyMsg.setText(receiveFriendRequest.getFriendRequestMessageArrayList().get(-1).getContent());
        }
        holder.tv_niceName.setText(receiveFriendRequest.getCommonUserInfo().getNickname());
        holder.bt_add.setVisibility(View.VISIBLE);
        holder.bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketClass.getInstance().getWebSocket().send((new Gson()).toJson(new SendAcceptRequest(receiveFriendRequestList.get(position).getFriendRequestsId(), MainApplication.getInstance().user.jyId, receiveFriendRequest.getCommonUserInfo().getJyId())));
                holder.bt_add.setVisibility(View.GONE);
                holder.tv_hasAdd.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return receiveFriendRequestList.size();
    }

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_avatar, iv_toward;
        public TextView tv_niceName, tv_applyMsg, tv_hasAdd;
        public Button bt_add;
        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_toward = itemView.findViewById(R.id.iv_toward);
            bt_add = itemView.findViewById(R.id.bt_add);
            tv_niceName = itemView.findViewById(R.id.tv_nickName);
            tv_applyMsg = itemView.findViewById(R.id.tv_applyMsg);
            tv_hasAdd = itemView.findViewById(R.id.tv_hasAdd);
        }
    }
}
