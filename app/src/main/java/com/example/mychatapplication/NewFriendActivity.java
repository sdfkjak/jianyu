package com.example.mychatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mychatapplication.adapter.FriendRequestAdapter;
import com.example.mychatapplication.commomclass.friendapplication.ReceiveFriendRequest;
import com.example.mychatapplication.customview.LeftImgButton;
import com.example.mychatapplication.database.UserInfo;
import com.example.mychatapplication.util.ToolbarUtil;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends AppCompatActivity{
    private Toolbar tb_head;
    private RecyclerView rv_friendRequests;
    private LeftImgButton bt_searchAccount;
    private FriendRequestAdapter friendRequestAdapter;
    private List<UserInfo> friendRequestList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);

        tb_head = findViewById(R.id.tb_head);
        tb_head.setNavigationIcon(R.drawable.back);
        tb_head.setTitle(R.string.new_friend);
        ToolbarUtil.setToolbarTitle(tb_head);
        setSupportActionBar(tb_head);

        tb_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_searchAccount = findViewById(R.id.bt_searchAccont);
        bt_searchAccount.scrollTo(0, 0);

        bt_searchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendActivity.this, AddFriendActivity.class));
            }
        });
        rv_friendRequests = findViewById(R.id.rv_friendRequests);
        friendRequestAdapter = new FriendRequestAdapter();
        rv_friendRequests.setLayoutManager(new LinearLayoutManager(this));
        rv_friendRequests.setAdapter(friendRequestAdapter);

        WebSocketClass.getInstance().getListMutableLiveData().observe(this, new Observer<ArrayList<ReceiveFriendRequest>>() {
            @Override
            public void onChanged(ArrayList<ReceiveFriendRequest> receiveFriendRequests) {
                friendRequestAdapter.setReceiveFriendRequestList(receiveFriendRequests);
                friendRequestAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.overflow_add_friend:
                startActivity(new Intent(this, AddFriendActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}