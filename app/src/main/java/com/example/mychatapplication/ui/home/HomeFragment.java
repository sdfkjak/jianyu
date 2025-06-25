package com.example.mychatapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.adapter.ChatItemAdapter;
import com.example.mychatapplication.model.ChatMessage;
import com.example.mychatapplication.databinding.FragmentHomeBinding;
import com.example.mychatapplication.model.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ChatItemAdapter chatItemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final RecyclerView rv_chatContainer = binding.rvChatContainer;
        rv_chatContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatItemAdapter = new ChatItemAdapter(getContext());
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAllUserLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                if(userList != null){
                    List<User> hasFriendChatUserList = new ArrayList<>();
                    for(User user: userList){
                        if(user.getFriendChatMessage() != null){
                            ChatMessage[] chatMessages = new Gson().fromJson(user.getFriendChatMessage(), ChatMessage[].class);
                            if(chatMessages.length != 0){
                                hasFriendChatUserList.add(user);
                            }
                        }
                    }
                    chatItemAdapter.setUserList(hasFriendChatUserList);
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