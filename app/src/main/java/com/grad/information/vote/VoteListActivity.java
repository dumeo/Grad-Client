package com.grad.information.vote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.grad.databinding.ActivityVoteListBinding;

public class VoteListActivity extends AppCompatActivity {
    private ActivityVoteListBinding mBinding;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVoteListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

    }

    private void initHandler(){};


    private void initData(){};


    private void initView(){}

    private void initListener(){
        mBinding.addVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoteListActivity.this, AddVoteActivity.class));
            }
        });
    }


}