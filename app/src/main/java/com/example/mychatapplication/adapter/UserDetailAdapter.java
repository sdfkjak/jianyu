package com.example.mychatapplication.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.ChatActivity;
import com.example.mychatapplication.R;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.util.ImageUtil;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.UserDetailViewHolder> {
    private UserInfo userInfo;
    private String itemString[] = {"设置备注和标签", "朋友权限", "朋友圈", "视频号", "更多信息", "发消息", "音视频通话"};

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @NonNull
    @Override
    public UserDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_user_detail, parent, false);
        return new UserDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailViewHolder holder, int position) {
        holder.itemView.setBackgroundResource(R.color.white);
        if(position == 0){
            holder.iv_avatar.setVisibility(View.VISIBLE);
            holder.tv_nickName.setVisibility(View.VISIBLE);
            holder.tv_jyId.setVisibility(View.VISIBLE);
            holder.tv_area.setVisibility(View.VISIBLE);
            holder.iv_avatar.setImageBitmap(ImageUtil.convertBase64ToBitmap(userInfo.getAvatar()));
            holder.tv_nickName.setText(userInfo.getNickname());
            holder.tv_jyId.setText(userInfo.getJyId());
            holder.tv_area.setText(userInfo.getArea());
        }else{
            if(itemString[position - 1].equals("发消息")){
                holder.tv_centerItem.setVisibility(View.VISIBLE);
                holder.tv_centerItem.setText(itemString[position - 1]);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ChatActivity.class);
                        intent.putExtra("jyId", userInfo.getJyId());
                        Log.d("图片长度2", userInfo.getJyId());
                        v.getContext().startActivity(intent);
                    }
                });
            } else if (itemString[position - 1].equals("音视频通话")) {
                holder.tv_centerItem.setVisibility(View.VISIBLE);
                holder.tv_centerItem.setText(itemString[position - 1]);
            }else{
                holder.tv_item.setVisibility(View.VISIBLE);
                holder.iv_forward.setVisibility(View.VISIBLE);
                holder.tv_item.setText(itemString[position - 1]);
            }
            if(itemString[position - 1].equals("朋友权限") || itemString[position - 1].equals("更多信息") || itemString[position - 1].equals("音视频通话")){
                holder.divider.setVisibility(View.GONE);
                DisplayMetrics displayMetrics = holder.tv_item.getContext().getResources().getDisplayMetrics();
                int screenHeight = displayMetrics.heightPixels;
                int spacingPy = (int) Math.round(screenHeight * 0.01);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                params.bottomMargin = spacingPy;
                holder.itemView.setLayoutParams(params);
            }
        }

    }

    @Override
    public int getItemCount() {
        return itemString.length + 1;
    }


    public static class UserDetailViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_nickName, tv_jyId, tv_area, tv_item, tv_centerItem;
        public ImageView iv_avatar, iv_forward;
        public View divider;
        public UserDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nickName = itemView.findViewById(R.id.tv_nickName);
            tv_jyId = itemView.findViewById(R.id.tv_jyId);
            tv_area = itemView.findViewById(R.id.tv_area);
            tv_item = itemView.findViewById(R.id.tv_item);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_forward = itemView.findViewById(R.id.iv_forward);
            tv_centerItem = itemView.findViewById(R.id.tv_centerItem);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
