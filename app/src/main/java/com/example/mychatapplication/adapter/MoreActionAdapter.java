package com.example.mychatapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;
import com.example.mychatapplication.ChatActivity;
import com.example.mychatapplication.R;
import com.example.mychatapplication.util.PermissionUtil;

public class MoreActionAdapter extends RecyclerView.Adapter<MoreActionAdapter.MoreActionViewHolder> {
    private Context context;
    private int[] moreActionIconItem = {R.drawable.photo, R.drawable.camera, R.drawable.location, R.drawable.voice,
            R.drawable.collection, R.drawable.namecard, R.drawable.file, R.drawable.music};
    private int[] moreActionNameItem = {R.string.photoAlbum, R.string.photography, R.string.location, R.string.voiceInput,
            R.string.myCollection, R.string.namecard, R.string.file, R.string.music};

    public MoreActionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MoreActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_more_action, parent, false);
        return new MoreActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreActionViewHolder holder, int position) {
        Resources resources = holder.itemView.getResources();
        holder.iv_img.setImageDrawable(resources.getDrawable(moreActionIconItem[position], null));
        holder.tv_item.setText(resources.getString(moreActionNameItem[position]));
        switch(position){
            case 0:
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(PermissionUtil.checkPermission(context, PermissionUtil.photoAlbumPermission, PermissionUtil.photoAlbumPermissionCode)){
                            new Intent(Intent.ACTION_GET_CONTENT)
                                    .addCategory(Intent.CATEGORY_OPENABLE)
                                    .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            ((ChatActivity) context).getSelectPhotoAlbumLauncher().launch("image/*");
                            Log.d("权限1", "success");
                        }else{
                            Log.d("权限1", "fail");
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return moreActionIconItem.length;
    }

    static class MoreActionViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_item;
        private ImageView iv_img;
        public MoreActionViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);
            iv_img = itemView.findViewById(R.id.iv_img);

        }
    }
}
