package com.grad.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityRegisterBinding;
import com.grad.information.MainPageActivity;
import com.grad.pojo.User;
import com.grad.constants.DefaultVals;
import com.grad.service.UserService;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

public class RegisterActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;
    private ProgressBar mProgressBar;
    private ActivityRegisterBinding mBinding;
    private boolean mLocalExists = false;
    private boolean mDatabaseExists = false;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        initHandler();
        checkUser();
        initView();
        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.releaseInstance();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UserConstants.CHECK_USER_OK:{
                        Log.e("wjj", "handler msg:" + (String)msg.obj);
                        mDatabaseExists = (((String)msg.obj).equals(UserConstants.USER_EXISTS)? true : false);
                        if(mDatabaseExists) {
                            startActivity(new Intent(RegisterActivity.this, MainPageActivity.class));
                            finish();
                        }
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void initView(){
        if(mUserViewModel == null){
            mUserViewModel = new UserViewModel(new User());
            mBinding.setUserViewModel(mUserViewModel);
        }
        mBinding.setLifecycleOwner(this);
        mUserViewModel.getRegisterStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(mProgressBar == null) mProgressBar = findViewById(R.id.register_progressbar);

                if(status.equals(DefaultVals.UNDER_REGISTER) || status.equals(DefaultVals.REGISTERING_FAILED) || status.equals(DefaultVals.REGISTERING_SUCCESS))
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    if(status.equals(DefaultVals.REGISTERING_SUCCESS)){
                        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
                        sharedPreferenceUtil.writeString(UserConstants.SHARED_PREF_USERINFO_KEY, JsonUtil.objectToJson(mUserViewModel.getUser()));
                        startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
                        finish();
                    }
                }

                else if(status.equals(DefaultVals.REGISTERING))
                    mProgressBar.setVisibility(View.VISIBLE);

                else if(status.equals(DefaultVals.REGISTER_UNFILL))
                    Toast.makeText(getApplicationContext(), "请将信息补充完整", Toast.LENGTH_SHORT).show();
            }
        });

        SpannableString spannableString = new SpannableString(mBinding.tvLogin.getText());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        mBinding.tvLogin.setText(spannableString);
        mBinding.tvLogin.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.teal_200));
    }



    private void checkUser(){
        String user = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE).readString(UserConstants.SHARED_PREF_USERINFO_KEY, null);
        if(user != null) mLocalExists = true;
        if(mLocalExists){
            User user_ = JsonUtil.jsonToObject(user, User.class);
            UserService.checkIfUserExists(mHandler, user_.getUid());
        }
    }

    private void initListener(){
        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }
}

