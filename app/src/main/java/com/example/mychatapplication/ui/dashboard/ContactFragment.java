package com.example.mychatapplication.ui.dashboard;

import static com.example.mychatapplication.util.Pinyin4jUtils.getAllPinyin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.adapter.ContactAdapter;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.databinding.FragmentDashboardBinding;
import com.example.mychatapplication.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactFragment extends Fragment{
    private FragmentDashboardBinding binding;
    private ContactAdapter contactAdapter;

    private RecyclerView rv_contact;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        rv_contact = binding.rvContact;
        View root = binding.getRoot();

        contactAdapter = new ContactAdapter(requireContext());
        rv_contact.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_contact.setAdapter(contactAdapter);
        ContactViewModel contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        contactViewModel.getAllUserLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for(User user: users){
                    Log.d("排序前", user.getNickname());
                }
                Collections.sort(users, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        String userInfo1Pinyin = getAllPinyin(user1.getNickname());
                        String userInfo2Pinyin = getAllPinyin(user2.getNickname());
                        int minLengthPinyin = Math.min(userInfo1Pinyin.length(), userInfo2Pinyin.length());
                        for (int i = 0; i < minLengthPinyin; i++) {
                            if(userInfo1Pinyin.charAt(i) != userInfo2Pinyin.charAt(i)){
                                return userInfo1Pinyin.charAt(i) - userInfo2Pinyin.charAt(i);
                            }else {
                                continue;
                            }
                        }
                        return 0;
                    }
                });
                for(User user: users){
                    Log.d("排序后", user.getNickname());
                }
                contactAdapter.setUserList(users);
                contactAdapter.notifyDataSetChanged();
            }
        });

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("收到消息A", "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("收到消息A", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("收到消息A", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("收到消息A", "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("收到消息A", "onDestroyView");
        binding = null;
    }
}