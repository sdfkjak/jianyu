package com.example.mychatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.mychatapplication.R;
import com.example.mychatapplication.commomclass.TalkItem;

import java.util.List;

public class TalkListAdapter extends BaseAdapter {
    private Context mContext;
    private List<TalkItem> mTalkItemList;

    public TalkListAdapter(Context context, List<TalkItem> talkItemList) {
        mContext = context;
        mTalkItemList = talkItemList;
    }
    public int getCount(){
        return mTalkItemList.size();
    }
    public Object getItem(int arg){
        return mTalkItemList.get(arg);
    }
    public long getItemId(int arg){
        return arg;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_talk, null);
            holder.tv_nickname = convertView.findViewById(R.id.tv_nickname);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_talkMeg = convertView.findViewById(R.id.tv_nicknameMeg);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        TalkItem talkItem = mTalkItemList.get(position);
        holder.tv_nickname.setText(talkItem.nickname);
        holder.tv_time.setText(talkItem.time);
        holder.tv_talkMeg.setText(talkItem.talkMeg);
        return convertView;
    }
    private class ViewHolder{
        public TextView tv_nickname, tv_time, tv_talkMeg;
    }

}
