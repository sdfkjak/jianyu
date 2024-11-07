package com.example.mychatapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.R;
import com.example.mychatapplication.model.FriendRequest;
import com.example.mychatapplication.model.sendWS.AcceptFriendRequest;
import com.example.mychatapplication.network.WebSocketService;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {
    private ArrayList<FriendRequest> friendRequestArrayList;
    private final Context context;
    public FriendRequestAdapter(Context context){
        this.context = context;
    }

    public void setReceiveFriendRequestList(ArrayList<FriendRequest> friendRequestList) {
        this.friendRequestArrayList = friendRequestList;
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
        FriendRequest friendRequest = friendRequestArrayList.get(position);
        RequestBuilder<Drawable> builder = Glide.with(context).load(new File(new File(context.getExternalFilesDir(null), "avatar"), friendRequest.getUser().getJyId()));
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
        builder.apply(options).into(holder.iv_avatar);
        if(friendRequest.getMode().equals("passive")){
            holder.iv_toward.setVisibility(View.INVISIBLE);
        }else{
            holder.iv_toward.setVisibility(View.VISIBLE);
        }
        if(friendRequest.getMessageArrayList().size() != 0){
            holder.tv_applyMsg.setText(friendRequest.getMessageArrayList().get(friendRequestArrayList.size() - 1).getContent());
        }
        holder.tv_niceName.setText(friendRequest.getUser().getNickname());
        holder.bt_add.setVisibility(View.VISIBLE);
        holder.bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebSocketService.getInstance().sendWSStringMsg((new Gson()).toJson(new AcceptFriendRequest(friendRequest.getFriendRequestsId(), MainApplication.getInstance().user.jyId, friendRequest.getUser().getJyId())));
                holder.bt_add.setVisibility(View.GONE);
                holder.tv_hasAdd.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequestArrayList.size();
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
