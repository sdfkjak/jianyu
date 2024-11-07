package com.example.mychatapplication;

import static com.example.mychatapplication.util.ToolbarUtil.setToolbarTitleCenter;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mychatapplication.adapter.TapPagerAdapter;
import com.example.mychatapplication.databinding.ActivityNavigationBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

public class NavigationActivity extends AppCompatActivity {
    private ActivityNavigationBinding binding;
    private Toolbar tb_head;
    private ViewPager2 vp2_fragments;
    private RadioGroup rg_title;
    private RadioButton rb_wechat, rb_contact, rb_mine;
    private TapPagerAdapter tapPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tb_head = binding.tbHead;
        rg_title = binding.rgTitle;
        rb_wechat = binding.rbWechat;
        rb_contact = binding.rbContact;
        rb_mine = binding.rbMine;
        vp2_fragments = binding.vp2Fragments;
        vp2_fragments.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        tapPagerAdapter = new TapPagerAdapter(this);
        vp2_fragments.setAdapter(tapPagerAdapter);
        setSupportActionBar(tb_head);
        setToolbarTitleCenter(tb_head);
        if(MainApplication.getInstance().user != null && MainApplication.getInstance().user.jyId != null && !MainApplication.getInstance().isStartTime){
            MainApplication.getInstance().startTime();
        }
        rg_title.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_wechat.setTextColor(getResources().getColor(R.color.black, null));
                rb_contact.setTextColor(getResources().getColor(R.color.black, null));
                rb_mine.setTextColor(getResources().getColor(R.color.black, null));
                switch (i){
                    case R.id.rb_wechat:
                        for(int k = 0; k < tb_head.getChildCount(); k++){
                            tb_head.getChildAt(k).setVisibility(View.VISIBLE);
                        }
                        tb_head.setBackgroundColor(getResources().getColor(R.color.evelengray, null));
                        tb_head.setTitle(R.string.title_home);
                        getWindow().setStatusBarColor(getResources().getColor(R.color.evelengray, null));
                        vp2_fragments.setCurrentItem(0);
                        rb_wechat.setTextColor(getResources().getColor(R.color.selectColor, null));
                        break;
                    case R.id.rb_contact:
                        for(int k = 0; k < tb_head.getChildCount(); k++){
                            tb_head.getChildAt(k).setVisibility(View.VISIBLE);
                        }
                        tb_head.setBackgroundColor(getResources().getColor(R.color.evelengray, null));
                        tb_head.setTitle(R.string.title_contact);
                        getWindow().setStatusBarColor(getResources().getColor(R.color.evelengray, null));
                        vp2_fragments.setCurrentItem(1);
                        rb_contact.setTextColor(getResources().getColor(R.color.selectColor, null));
                        break;
                    case R.id.rb_mine:
                        for(int k = 0; k < tb_head.getChildCount(); k++){
                            tb_head.getChildAt(k).setVisibility(View.INVISIBLE);
                        }
                        tb_head.setBackground(getResources().getDrawable(R.color.white, null));
                        getWindow().setStatusBarColor(getResources().getColor(R.color.white, null));
                        vp2_fragments.setCurrentItem(2);
                        rb_mine.setTextColor(getResources().getColor(R.color.selectColor, null));
                        break;
                }
            }
        });
        vp2_fragments.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("posssposss", String.valueOf(position));
                rg_title.check(rg_title.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 从menu_overflow.xml中构建菜单界面布局
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId(); // 获取菜单项的编号
//        if (id == android.R.id.home) { // 点击了工具栏左边的返回箭头
//            finish(); // 结束当前页面
//        } else if (id == R.id.menu_refresh) { // 点击了刷新图标
//            tv_desc.setText("当前刷新时间: " + DateUtil.getNowTime());
//        } else if (id == R.id.menu_about) { // 点击了关于菜单项
//            Toast.makeText(this, "这个是工具栏的演示demo", Toast.LENGTH_LONG).show();
//        } else if (id == R.id.menu_quit) { // 点击了退出菜单项
//            finish(); // 结束当前页面
//        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getTb_head() {
        return tb_head;
    }
}