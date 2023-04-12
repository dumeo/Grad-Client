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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityToVoteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mVoteId = getIntent().getStringExtra("voteId");
        initHandler();
        initData();

    }
    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case VoteConstants.GET_VOTE_OK:{
                        Log.e("wjj", "get vote ok");
                        mVoteItem = (VoteItem) msg.obj;
                        initView();
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
        hideOptions();
        displayVoteRes();
    }

    private void hideOptions() {
        mBinding.rlOp1.rlTop.setVisibility(View.INVISIBLE);
        mBinding.rlOp2.rlTop.setVisibility(View.INVISIBLE);
    }


    private void displayVoteRes(){
        List<VoteOption> voteOptionList = mVoteItem.getVoteOptions();
        String votedOptionId = mVoteItem.getClientToThisInfo().getOptionId();
        mBinding.opRes1.setText(voteOptionList.get(0).getOptionContent());
//        mBinding.opRes1.setMax((int) mVoteItem.getVote().getVoteCnt());
//        customProgressBar1.setProgress((int)voteOptionList.get(0).getCnt());
        if(votedOptionId != null && votedOptionId.equals(voteOptionList.get(0).getOptionId()))
            mBinding.opRes1.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg2));
        else mBinding.opRes1.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg));
        mBinding.opRes1.setMax(100);
        mBinding.opRes1.setProgress(32);

        mBinding.opRes2.setText(voteOptionList.get(1).getOptionContent());
//        mBinding.opRes2.setMax((int) mVoteItem.getVote().getVoteCnt());
//        customProgressBar2.setProgress((int)voteOptionList.get(1).getCnt());
        if(votedOptionId != null && votedOptionId.equals(voteOptionList.get(1).getOptionId()))
            mBinding.opRes2.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg2));
        else mBinding.opRes2.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg));
        mBinding.opRes2.setMax(100);
        mBinding.opRes2.setProgress(78);
        mBinding.opRes1.setVisibility(View.VISIBLE);
        mBinding.opRes2.setVisibility(View.VISIBLE);

//        List<CustomProgressBar> otherOptions = new ArrayList<>();
//
//        for(int i = 2; i < voteOptionList.size(); i ++){
//            VoteOption voteOption = mVoteItem.getVoteOptions().get(i);
//            CustomProgressBar customProgressBar_ = new CustomProgressBar(ToVoteActivity.this, null, android.R.attr.progressBarStyleHorizontal);
//            if(votedOptionId != null && votedOptionId.equals(voteOption.getVoteId()))
//                customProgressBar_.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg2));
//            else customProgressBar_.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bar_bg));
//            customProgressBar_.setId(View.generateViewId());
//            otherOptions.add(customProgressBar_);
//        }
//            int w = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_width);
//            int h = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_height);
//            int marL = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_progress_marL);
//            int marT = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_option_mar_top);
//        for(int i = 0;i < otherOptions.size(); i ++){
//            CustomProgressBar customProgressBar = otherOptions.get(i);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
//            if(i == 0) params.addRule(RelativeLayout.BELOW, mBinding.opRes2.getId());
//            else params.addRule(RelativeLayout.BELOW, otherOptions.get(i - 1).getId());
//            params.setMargins(marL, marT, 0, 0);
//            customProgressBar.setLayoutParams(params);
//            mBinding.optionHolder.addView(customProgressBar);
//        }
    }

    private void initListener(){};
}