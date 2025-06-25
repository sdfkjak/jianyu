package com.example.mychatapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PersonalInfoAdapter extends RecyclerView.Adapter<PersonalInfoAdapter.MyViewHolder> {
    private List<PersonalInfo> personalInfoList = new ArrayList<>();
    private Context context;

    public PersonalInfoAdapter(Context context) {
        this.context = context;
    }

    public void setPersonalInfoList(List<PersonalInfo> personalInfoList) {
        this.personalInfoList = personalInfoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_presonalinfo, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PersonalInfo personalInfo = personalInfoList.get(position);
        holder.itemView.setBackgroundResource(R.color.white);
        holder.tv_itemName.setText(personalInfo.getItemName());
        if(personalInfo.getItemInfo() != null && personalInfo.getItemName() != R.string.avatar){
            holder.tv_itemInfo.setVisibility(View.VISIBLE);
            holder.tv_itemInfo.setText(personalInfo.getItemInfo());
        } else if (personalInfo.getQRCode() == R.drawable.baseline_qr_code_24) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.iv_img.getLayoutParams();
            params.height = 100;
            params.width = 100;
            holder.iv_img.setLayoutParams(params);
            holder.iv_img.setVisibility(View.VISIBLE);
            holder.iv_img.setImageResource(personalInfo.getQRCode());
        }
        if(personalInfo.getItemName() == R.string.moreInfo || personalInfo.getItemName() == R.string.ringtones || personalInfo.getItemName() == R.string.jyBean){
            holder.divider.setVisibility(View.GONE);
            DisplayMetrics displayMetrics = holder.tv_itemInfo.getContext().getResources().getDisplayMetrics();
            int screenHeight = displayMetrics.heightPixels;
            int spacingPy = (int) Math.round(screenHeight * 0.01);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            params.bottomMargin = spacingPy;
            holder.itemView.setLayoutParams(params);
        }

        if(personalInfo.getItemName() == R.string.avatar){
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.iv_img.getLayoutParams();
            params.height = 200;
            params.width = 200;
            holder.iv_img.setLayoutParams(params);
            holder.iv_img.setVisibility(View.VISIBLE);
            RequestBuilder<Drawable> builder = Glide.with(context).load(new File(MainApplication.getInstance().avatarFolder, MainApplication.getInstance().user.jyId));
            RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
            builder.apply(options).into(holder.iv_img);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PersonInfoActivity) v.getContext()).requestPermission();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return personalInfoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_img; // 声明行星图片的图像视图对象
        public ImageView iv_forward; // 声明行星图片的图像视图对象
        public TextView tv_itemName; // 声明行星名称的文本视图对象
        public TextView tv_itemInfo; // 声明行星描述的文本视图对象
        public View divider;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            iv_forward = itemView.findViewById(R.id.iv_forward);
            tv_itemName = itemView.findViewById(R.id.tv_itemName);
            tv_itemInfo = itemView.findViewById(R.id.tv_itemInfo);
            divider = itemView.findViewById(R.id.divider);
        }

    }
}
