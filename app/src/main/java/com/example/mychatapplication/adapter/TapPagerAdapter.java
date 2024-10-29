package com.example.mychatapplication.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mychatapplication.ui.dashboard.DashboardFragment;
import com.example.mychatapplication.ui.home.HomeFragment;
import com.example.mychatapplication.ui.notifications.NotificationsFragment;

public class TapPagerAdapter extends FragmentStateAdapter {

    public TapPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new DashboardFragment();
            case 2:
                return new NotificationsFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
