package com.example.mychatapplication.ui.dashboard;

import static com.example.mychatapplication.util.Pinyin4jUtils.getAllPinyin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.NewFriendActivity;
import com.example.mychatapplication.adapter.ContactAdapter;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.database.UserInfoRepository;
import com.example.mychatapplication.databinding.FragmentDashboardBinding;
import com.example.mychatapplication.ui.home.HomeFragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DashboardFragment extends Fragment{
    private FragmentDashboardBinding binding;
    private ContactAdapter contactAdapter;

    private RecyclerView rv_contact;
    private List<UserInfo> userInfoList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("收到消息A", "onCreateView");
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        rv_contact = binding.rvContact;
        View root = binding.getRoot();

        contactAdapter = new ContactAdapter();
        rv_contact.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_contact.setAdapter(contactAdapter);
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getAllUserInfoLiveData().observe(this, new Observer<List<UserInfo>>() {
            @Override
            public void onChanged(List<UserInfo> userInfos) {
                for(UserInfo userInfo: userInfos){
                    Log.d("排序前", userInfo.getNickname());
                }
                Collections.sort(userInfos, new Comparator<UserInfo>() {
                    @Override
                    public int compare(UserInfo userInfo1, UserInfo userInfo2) {
                        String userInfo1Pinyin = getAllPinyin(userInfo1.getNickname());
                        String userInfo2Pinyin = getAllPinyin(userInfo2.getNickname());
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
                for(UserInfo userInfo: userInfos){
                    Log.d("排序后", userInfo.getNickname());
                }
                contactAdapter.setUserInfos(userInfos);
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