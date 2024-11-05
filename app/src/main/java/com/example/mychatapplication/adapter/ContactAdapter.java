package com.example.mychatapplication.adapter;

import static com.example.mychatapplication.util.Pinyin4jUtils.getFirstPinYin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.mychatapplication.NewFriendActivity;
import com.example.mychatapplication.R;
import com.example.mychatapplication.UserDetailActivity;
import com.example.mychatapplication.model.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private List<User> userList = new ArrayList<>();
    private final static String[] commonItem = {"新的朋友", "群聊", "标签", "公众号"};

    private Context context;

    public ContactAdapter(Context context) {
        this.context = context;
    }

    private List<AllContactItem> allContactItem = new ArrayList<>();

    public void setUserList(List<User> userList) {
        this.userList = userList;
        allContactItem.clear();
        initAllContactItem();
    }

    private void initAllContactItem() {
        for (int i = 0; i < commonItem.length; i++) {
            allContactItem.add(new AllContactItem("commonItem", null));
        }

        List<String> characters = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            String FirstPinYin = getFirstPinYin(userList.get(i).getNickname());
            if (!characters.contains(FirstPinYin)) {
                characters.add(FirstPinYin);
                Log.d("bubaohan", FirstPinYin);
            }

        }

        for (int i = 0; i < characters.size(); i++) {
            char currentChar = characters.get(i).toCharArray()[0];
            allContactItem.add(new AllContactItem("character", currentChar));
            for (User user : userList) {
                if (getFirstPinYin(user.getNickname()).equals(String.valueOf(currentChar))) {
                    allContactItem.add(new AllContactItem("contact", user));
                }
            }
        }
    }

    public List<User> getUserList() {
        return userList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = layoutInflater.inflate(R.layout.item_character, parent, false);
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.item_contact, parent, false);
                break;
            default:
                view = null;
        }


        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        AllContactItem contactItem = allContactItem.get(position);
        switch (contactItem.getType()) {
            case "character":
                holder.tv_character.setText(String.valueOf(contactItem.getCharacter()));
                break;
            case "commonItem":
                holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.color.white, null));
                switch (position) {
                    case 0:
                        holder.iv_avatar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.new_friend, null));
                        holder.tv_nickname.setText(commonItem[0]);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                view.getContext().startActivity(new Intent(view.getContext(), NewFriendActivity.class));
                            }
                        });
                        holder.divider.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holder.iv_avatar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.chat_group, null));
                        holder.tv_nickname.setText(commonItem[1]);
                        holder.divider.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        holder.iv_avatar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.label, null));
                        holder.tv_nickname.setText(commonItem[2]);
                        holder.divider.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        holder.iv_avatar.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.offical_account, null));
                        holder.tv_nickname.setText(commonItem[3]);
                        holder.divider.setVisibility(View.GONE);
                        break;
                }
                break;
            case "contact":
                holder.itemView.setBackground(holder.itemView.getResources().getDrawable(R.color.white, null));
                User user = allContactItem.get(position).getUser();
                RequestBuilder<Drawable> builder = Glide.with(context).load(new File(MainApplication.getInstance().avatarFolder, user.getJyId()));
                RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(30));
                builder.apply(options).into(holder.iv_avatar);
                holder.tv_nickname.setText(user.getNickname());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), UserDetailActivity.class);
                        intent.putExtra("jyId", user.getJyId());
                        v.getContext().startActivity(intent);
                    }
                });
                Log.d("长度", String.valueOf(allContactItem.size()));
                Log.d("长度", String.valueOf(position));
                if (allContactItem.size() - 1 == position) {
                    holder.divider.setVisibility(View.GONE);
                }
        }
    }

    @Override
    public int getItemCount() {
        return allContactItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (allContactItem.get(position).getType()) {
            case "character":
                return 0;
            case "commonItem":
            case "contact":
            default:
                return 1;
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_avatar;
        public TextView tv_nickname, tv_character;
        private View divider;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            divider = itemView.findViewById(R.id.divider);
            tv_character = itemView.findViewById(R.id.tv_character);
        }
    }

    class AllContactItem {
        private String type;
        private User user;
        private char character;

        public String getType() {
            return type;
        }

        public User getUser() {
            return user;
        }

        public char getCharacter() {
            return character;
        }

        public AllContactItem(String type, User user) {
            this.type = type;
            this.user = user;
        }

        public AllContactItem(String type, char character) {
            this.type = type;
            this.character = character;
        }
    }
}
