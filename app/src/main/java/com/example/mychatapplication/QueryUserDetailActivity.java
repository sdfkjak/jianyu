package com.example.mychatapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;


import com.example.mychatapplication.adapter.SearchUserDetailAdapter;
import com.example.mychatapplication.model.User;

public class QueryUserDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private SearchUserDetailAdapter searchUserDetailAdapter;
    private RecyclerView recyclerView;
    private Button bt_addToPhonebook;
    private Toolbar tb_head;
    private User searchUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_user_detail);

        tb_head = findViewById(R.id.tb_head);
        tb_head.setNavigationIcon(getResources().getDrawable(R.drawable.back, null));
        tb_head.setTitle("");
        setSupportActionBar(tb_head);
        tb_head.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.rv_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchUser = (User) getIntent().getExtras().getSerializable("searchUser");
        searchUserDetailAdapter = new SearchUserDetailAdapter(this, searchUser);
        recyclerView.setAdapter(searchUserDetailAdapter);
        bt_addToPhonebook = findViewById(R.id.bt_addtocontact);
        bt_addToPhonebook.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        if(v.getId() == R.id.bt_addtocontact){
            Intent intent = new Intent(this, ApplyAddFriendActivity.class);
            intent.putExtra("target", searchUser.getJyId());
            startActivity(intent);
        }
    }

}