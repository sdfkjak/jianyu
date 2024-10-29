package com.example.mychatapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.adapter.ChatItemAdapter;
import com.example.mychatapplication.commomclass.PrivateChat.FriendChatMessage;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ChatItemAdapter chatItemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final RecyclerView rv_chatContainer = binding.rvChatContainer;
        rv_chatContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatItemAdapter = new ChatItemAdapter(getContext());
//        rv_chatContainer.setAdapter(chatItemAdapter);
        homeViewModel.getFriendChatMessageList().observe(this, new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(List<UserInfo> userInfos) {
                if(userInfos != null){
                    List<UserInfo> hasFriendChatUserInfoList = new ArrayList<>();
                    for(UserInfo userInfo: userInfos){
                        if(userInfo.getFriendChatMessages() != null){
                            FriendChatMessage[] friendChatMessages = new Gson().fromJson(userInfo.getFriendChatMessages(), FriendChatMessage[].class);
                            if(friendChatMessages.length != 0){
                                hasFriendChatUserInfoList.add(userInfo);
                            }
                        }
                    }

                    chatItemAdapter.setUserInfoList(hasFriendChatUserInfoList);
                    rv_chatContainer.setAdapter(chatItemAdapter);
                    chatItemAdapter.notifyDataSetChanged();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}