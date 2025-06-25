package com.example.mychatapplication.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapplication.MainApplication;
import com.example.mychatapplication.adapter.MineAdapter;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.databinding.FragmentNotificationsBinding;
import com.example.mychatapplication.model.User;

public class NotificationsFragment extends Fragment {
    private final static String tag = "NotificationsFragment";
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
        notificationsViewModel.getUserLiveData(MainApplication.getInstance().user.jyId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null){
                    mineAdapter.setUser(user);
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