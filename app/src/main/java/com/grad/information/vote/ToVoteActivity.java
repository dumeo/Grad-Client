package com.grad.information.vote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.constants.VoteConstants;
import com.grad.databinding.ActivityToVoteBinding;
import com.grad.databinding.VoteOptionBinding;
import com.grad.pojo.User;
import com.grad.pojo.vote.VoteItem;
import com.grad.pojo.vote.VoteOption;
import com.grad.service.VoteService;
import com.grad.util.DimensUtil;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class ToVoteActivity extends AppCompatActivity {
    ActivityToVoteBinding mBinding;
    private VoteItem mVoteItem;
    private String mVoteId;
    private User mUser;
    private Handler mHandler;
    List<CustomProgressBar> mVoteRes = new ArrayList<>();
    List<View> mVoteOptions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityToVoteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mVoteId = getIntent().getStringExtra("voteId");
        initHandler();
        initData();
        initListener();

    }
    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case VoteConstants.GET_VOTE_OK:{
                        mVoteItem = (VoteItem) msg.obj;
                        initVoteOptions();
                        initVoteRes();
                        initView();
                        break;
                    }
                    case VoteConstants.VOTE_OK:{
                        mVoteItem = (VoteItem) msg.obj;
                        hideVoteOptions();
                        initVoteRes();
                        showVoteRes();
                        break;
                    }
                }
                return false;
            }
        });

    };

    private void initData(){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
        mUser = JsonUtil.jsonToObject(sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, "null"), User.class);
        VoteService.getVoteById(mHandler, mUser.getUid(), mVoteId);

    };
    private void initView(){
        mBinding.userTop.userName.setText(mVoteItem.getUser().getUsername());
        mBinding.userTop.userAddr.setText(mVoteItem.getUser().getHouseAddr());
        mBinding.voteTitle.setText(mVoteItem.getVote().getVoteTitle());
        mBinding.voteContent.setText(mVoteItem.getVote().getVoteContent());
        mBinding.voteCount.setText(mVoteItem.getVote().getVoteCnt() + "");
        if(mVoteItem.getClientToVoteInfo().getOptionId() != null ||
                mVoteItem.getVote().getVoteStatus().equals(VoteConstants.VOTE_STATUS_ENDED))
        {
            showVoteRes();
        }
        else showVoteOptions();
    }

    private void initListener(){
        mBinding.userTop.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    };



    public void initVoteRes(){
        mVoteRes.clear();
        List<VoteOption> voteOptionList = mVoteItem.getVoteOptions();
        String votedOptionId = mVoteItem.getClientToVoteInfo().getOptionId();
        List<CustomProgressBar> tmp = new ArrayList<>();
        for(int i = 0; i < voteOptionList.size(); i ++){
            VoteOption voteOption = mVoteItem.getVoteOptions().get(i);
            CustomProgressBar customProgressBar_ = new CustomProgressBar(ToVoteActivity.this, null, android.R.attr.progressBarStyleHorizontal);
            if(votedOptionId != null && votedOptionId.equals(voteOption.getOptionId()))
                customProgressBar_.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg2));
            else customProgressBar_.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg));
            //----
            customProgressBar_.setText(voteOptionList.get(i).getOptionContent());
            customProgressBar_.setMax((int) mVoteItem.getVote().getVoteCnt());
            customProgressBar_.setProgress((int) voteOptionList.get(i).getCnt());
            //---
            customProgressBar_.setId(View.generateViewId());
            tmp.add(customProgressBar_);
        }
        int w = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_width) * 3;
        int h = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_height) * 3;
        int marL = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_marL) * 3;
        int marT = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_option_mar_top) * 3;
        for(int i = 0;i < tmp.size(); i ++){
            CustomProgressBar customProgressBar = tmp.get(i);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            if(i > 0) params.addRule(RelativeLayout.BELOW, tmp.get(i - 1).getId());
            params.setMargins(marL, marT, 0, 0);
            customProgressBar.setLayoutParams(params);
            mVoteRes.add(customProgressBar);
        }
    }
    public void initVoteOptions(){
        List<VoteOption> voteOptionList = mVoteItem.getVoteOptions();
        List<View> tmp = new ArrayList<>();
        for(int i = 0; i < voteOptionList.size(); i ++){
            VoteOption voteOption = voteOptionList.get(i);
            VoteOptionBinding binding = VoteOptionBinding.inflate(getLayoutInflater());
            binding.btVote.setOptionPos(i);
            binding.optionContent.setText(voteOption.getOptionContent());
            binding.rlTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VoteOption voteOption_ = voteOptionList.get(binding.btVote.getOptionPos());
                    VoteService.vote(mHandler, mUser.getUid(), voteOption_.getVoteId(), voteOption_.getOptionId());
                }
            });
            binding.getRoot().setId(View.generateViewId());
            tmp.add(binding.getRoot());
        }
        int w = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_width) * 3;
        int h = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_height) * 3;
        int marL = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_marL) * 3;
        int marT = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_option_mar_top) * 3;

        for(int i = 0;i < tmp.size(); i ++){
            View view = tmp.get(i);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
            if(i > 0) params.addRule(RelativeLayout.BELOW, tmp.get(i - 1).getId());
            params.setMargins(marL, marT, 0, 0);
            view.setLayoutParams(params);
            mVoteOptions.add(view);
        }
    }

    private void hideVoteOptions() {
        for(View view : mVoteOptions) view.setVisibility(View.INVISIBLE);
    }

    private void showVoteOptions(){
        for(View view : mVoteOptions){
            mBinding.optionHolder.addView(view);
        }
    }
    private void hideVoteRes(){
        for(CustomProgressBar customProgressBar : mVoteRes)
            customProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showVoteRes(){
        for(CustomProgressBar customProgressBar : mVoteRes)
            mBinding.optionHolder.addView(customProgressBar);
    }


}