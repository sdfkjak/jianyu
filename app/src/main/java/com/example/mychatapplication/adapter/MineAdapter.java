package com.example.mychatapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
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
import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.PersonInfoActivity;
import com.example.mychatapplication.R;
import com.example.mychatapplication.SettingActivity;
import com.example.mychatapplication.model.User;
import com.example.mychatapplication.util.ImageUtil;

import java.io.File;

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.MineViewHolder> {
    private User user;
    private Context context;
    private static final String[] mineCommonItem = {"服务", "收藏", "朋友圈", "卡包", "表情", "设置"};

    public MineAdapter(Context context) {
        this.context = context;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public MineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case 0:
                view = layoutInflater.inflate(R.layout.item_personal_info_card, parent, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.item_common_function, parent, false);
                break;
            default:
                view = null;
        }
        return new MineViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MineViewHolder holder, int position) {
        holder.itemView.setBackgroundResource(R.color.white);
        DisplayMetrics displayMetrics = holder.itemView.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int spacingPy = (int) Math.round(height * 0.01);
        switch (position){
            case 0:
                RequestBuilder<Drawable> builder = Glide.with(context).load(new File(MainApplication.getInstance().avatarFolder, user.getJyId()));
                RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
                builder.apply(options).into(holder.iv_avatar);
                holder.tv_nickname.setText(user.getNickname());
                holder.tv_jyId.setText(user.getJyId());
                ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                param.bottomMargin = spacingPy;
                holder.itemView.setLayoutParams(param);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.getContext().startActivity(new Intent(view.getContext(), PersonInfoActivity.class));
                    }
                });
                break;
            case 1:
                holder.iv_img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.service, null));
                holder.tv_item.setText(mineCommonItem[position - 1]);
                ViewGroup.MarginLayoutParams param1 = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                param1.bottomMargin = spacingPy;
                holder.itemView.setLayoutParams(param1);
                holder.divider.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.iv_img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.collect, null));
                holder.tv_item.setText(mineCommonItem[position - 1]);
                break;
            case 3:
                holder.iv_img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.friendcircle, null));
                holder.tv_item.setText(mineCommonItem[position - 1]);
                break;
            case 4:
                holder.iv_img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable._____weixin_cards_offers, null));
                holder.tv_item.setText(mineCommonItem[position - 1]);
                break;
            case 5:
                holder.iv_img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.expression, null));
                holder.tv_item.setText(mineCommonItem[position - 1]);
                ViewGroup.MarginLayoutParams param2 = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                param2.bottomMargin = spacingPy;
                holder.itemView.setLayoutParams(param2);
                holder.divider.setVisibility(View.INVISIBLE);
                break;
            case 6:
                holder.iv_img.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.setting, null));
                holder.tv_item.setText(mineCommonItem[position - 1]);
                holder.divider.setVisibility(View.INVISIBLE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), SettingActivity.class));
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mineCommonItem.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0? 0: 1;
    }

    public class MineViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_avatar, iv_QRCode, iv_forward, iv_img;
        private TextView tv_nickname, tv_jyId, tv_item;
        private View divider;
        public MineViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_QRCode = itemView.findViewById(R.id.iv_QRCode);
            iv_forward = itemView.findViewById(R.id.iv_forward);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_jyId = itemView.findViewById(R.id.tv_jyId);
            tv_item = itemView.findViewById(R.id.tv_item);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
