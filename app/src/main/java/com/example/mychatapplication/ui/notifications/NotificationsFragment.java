package com.example.mychatapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.NavigationActivity;
import com.example.mychatapplication.PersonInfoActivity;
import com.example.mychatapplication.R;
import com.example.mychatapplication.adapter.MineAdapter;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.databinding.FragmentNotificationsBinding;
import com.example.mychatapplication.ui.dashboard.DashboardFragment;
import com.example.mychatapplication.util.ImageUtil;

import java.util.Random;

public class NotificationsFragment extends Fragment {
    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;
    private RecyclerView rv_container;
    private MineAdapter mineAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        mineAdapter = new MineAdapter(getActivity());
        rv_container = binding.rvContainer;
        rv_container.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsViewModel.getCurrentUserInfoLiveData(MainApplication.getInstance().user.jyId).observe(getActivity(), new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                if(userInfo != null){
                    mineAdapter.setUserInfo(userInfo);
                    rv_container.setAdapter(mineAdapter);
                    mineAdapter.notifyDataSetChanged();
                }
            }
        });

        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}