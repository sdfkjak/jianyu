package com.example.mychatapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.example.mychatapplication.R;
import com.example.mychatapplication.model.User;

import java.io.File;

public class SearchUserDetailAdapter extends RecyclerView.Adapter<SearchUserDetailAdapter.QueryUserDetailViewHolder> {
    private User searchUser;
    private Context context;
    private String[] items = {"设置备注和标签", "个性签名", "来源"};

    public SearchUserDetailAdapter(Context context, User searchUser) {
        this.context = context;
        this.searchUser = searchUser;
    }

    @NonNull
    @Override
    public QueryUserDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType){
            case 0:
                view = layoutInflater.inflate(R.layout.item_query_user_info_detail_card, parent, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.item_query_user_info_detail, parent, false);
                break;
            default:
                view = null;
        }
        return new QueryUserDetailViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull QueryUserDetailViewHolder holder, int position) {
        holder.itemView.setBackgroundResource(R.color.white);
        if(position == 0){
//            holder.iv_avatar.setImageBitmap(BitmapFactory.decodeByteArray(searchUser.getAvatarBytes(), 0, searchUser.getAvatarBytes().length));
            RequestBuilder<Drawable> builder = Glide.with(context).load(new File(MainApplication.getInstance().cacheFolder, searchUser.getJyId()));
            RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
            builder.apply(options).into(holder.iv_avatar);
            holder.tv_nickname.setText(searchUser.getNickname());
            holder.tv_area.setText(searchUser.getArea());
            Drawable drawable;
            if(searchUser.getSex().equals("0")){
                drawable = context.getResources().getDrawable(R.drawable.baseline_female_24, null);
            }else{
                drawable = context.getResources().getDrawable(R.drawable.baseline_male_24, null);
            }
            holder.iv_sex.setImageDrawable(drawable);
        }else{
            Log.d("asdfasdfasf", items[position - 1]);
            if(items[position - 1].equals("设置备注和标签") || items[position - 1].equals("来源")){
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
                DisplayMetrics displayMetrics = holder.itemView.getResources().getDisplayMetrics();
                int height = displayMetrics.heightPixels;
                int spacingY = (int) Math.round(height * 0.01);
                params.bottomMargin = spacingY;
                holder.itemView.setLayoutParams(params);
            }
            holder.tv_item.setText(items[position - 1]);
        }
    }

    @Override
    public int getItemCount() {
        return items.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }
        return 1;
    }

    public static class QueryUserDetailViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_avatar, iv_sex;
        public TextView tv_nickname, tv_area, tv_item;
        public QueryUserDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            iv_sex = itemView.findViewById(R.id.iv_sex);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_area = itemView.findViewById(R.id.tv_area);
            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }
}
