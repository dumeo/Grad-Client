package com.grad.information.vote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.constants.VoteConstants;
import com.grad.databinding.ActivityAddVoteBinding;
import com.grad.pojo.User;
import com.grad.pojo.vote.Vote;
import com.grad.pojo.vote.VoteItem;
import com.grad.pojo.vote.VoteOption;
import com.grad.service.VoteService;
import com.grad.util.DimensUtil;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.hutool.core.util.StrUtil;

public class AddVoteActivity extends AppCompatActivity {
    ActivityAddVoteBinding mBinding;
    private Handler mHandler;
    private User mUser;
    private List<AddedOption> mAddedOptions = new ArrayList<>();
    private String mEndDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddVoteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initData();
        initView();
        initListener();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case VoteConstants.ADD_VOTE_OK:{
                        Log.e("wjj", "add vote ok");
                        finish();
                        break;
                    }
                }

                return false;
            }
        });

    };


    private void initData(){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
        String userJson = sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, "null");
        mUser = JsonUtil.jsonToObject(userJson, User.class);
    };


    private void initView(){
        final Calendar calendar = Calendar.getInstance();
        mEndDate = calendar.get(Calendar.YEAR)
                + "-"
                + ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1))
                + "-"
                + calendar.get(Calendar.DAY_OF_MONTH)
                + " 23:59:59";

        mBinding.btRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkEmpty()){
                    Toast.makeText(AddVoteActivity.this, "请将信息补充完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Vote vote = new Vote(null, mUser.getUid(),
                            0, mBinding.voteTitle.getText().toString(),
                            mBinding.voteContent.getText().toString(),
                            null, 0, 0, null,
                            null, mEndDate);
                    List<VoteOption> voteOptions = new ArrayList<>();
                    VoteOption voteOption1 = new VoteOption();
                    voteOption1.setOptionContent(mBinding.op1.getText().toString());
                    voteOption1.setOptionOrder(0);
                    VoteOption voteOption2 = new VoteOption();
                    voteOption2.setOptionContent(mBinding.op2.getText().toString());
                    voteOption2.setOptionOrder(1);
                    voteOptions.add(voteOption1);
                    voteOptions.add(voteOption2);
                    int pos = 2;
                    for(int i = 0; i < mAddedOptions.size(); i ++){
                        if(!StrUtil.isEmpty(mAddedOptions.get(i).getEditText().getText().toString())){
                            VoteOption voteOption = new VoteOption();
                            voteOption.setOptionContent(mAddedOptions.get(i).getEditText().getText().toString());
                            voteOption.setOptionOrder(pos);
                            voteOptions.add(voteOption);
                            pos ++;
                        }
                    }

                    VoteItem voteItem = new VoteItem(vote, mUser, null, voteOptions);
                    VoteService.addVote(mHandler, voteItem);
                }
            }
        });

        mBinding.ivCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.datePicker.setVisibility(View.VISIBLE);
                mBinding.btCalenderOk.setVisibility(View.VISIBLE);
                mBinding.nestedScrv.fullScroll(View.FOCUS_DOWN);
            }
        });

        mBinding.datePicker.setMinDate(calendar.getTimeInMillis());
        mBinding.datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mEndDate = year
                        + "-"
                        + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1)
                        + "-"
                        + dayOfMonth
                        + " 23:59:59";
                mBinding.selectedDate.setText(mEndDate.substring(0, 10));
            }
        });
        mBinding.btCalenderOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.datePicker.setVisibility(View.INVISIBLE);
                mBinding.btCalenderOk.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean checkEmpty() {
        if(StrUtil.isEmpty(mBinding.op1.getText().toString())
        || StrUtil.isEmpty(mBinding.op2.getText().toString())
        || StrUtil.isEmpty(mBinding.voteTitle.getText().toString())
        ) return false;
        return true;
    }

    private void initListener(){
        mBinding.addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViewToLn();
            }
        });

    }

    private void addViewToLn(){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText editText = new EditText(this);
        editText.setBackgroundResource(R.drawable.et_background);
        editText.setTextSize(17);
        int width = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_option_width) * 3;
        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextParams.setMargins(10, 70, 0, 0);
        editText.setLayoutParams(editTextParams);
        OptionDeleteIV optionDeleteIV = new OptionDeleteIV(this);
        int width2 = DimensUtil.getValues(getApplicationContext(), R.dimen.vote_option_delete_size);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(width2, width2);
        ivParams.setMargins(30, 100, 0, 0);
        optionDeleteIV.setLayoutParams(ivParams);
        optionDeleteIV.setPos(mAddedOptions.size());
        optionDeleteIV.setImageResource(R.mipmap.minus);
        optionDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOption(optionDeleteIV.getPos());
            }
        });
        linearLayout.addView(editText);
        linearLayout.addView(optionDeleteIV);
        AddedOption addedOption = new AddedOption(editText, optionDeleteIV, linearLayout);
        mAddedOptions.add(addedOption);
        mBinding.addedOptionHolder.addView(linearLayout);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        editText.requestFocus();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void deleteOption(int pos) {
        mAddedOptions.remove(pos);
        mBinding.addedOptionHolder.removeAllViews();
        reArrangeOptions();
    }

    private void reArrangeOptions() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        for(int i = 0;i < mAddedOptions.size(); i ++){
            AddedOption addedOption = mAddedOptions.get(i);
            addedOption.getOptionDeleteIV().setPos(i);
            mBinding.addedOptionHolder.addView(addedOption.getLinearLayout());
            if(i == mAddedOptions.size() - 1){
                addedOption.getEditText().requestFocus();
                imm.showSoftInput(addedOption.getEditText(), InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }
}