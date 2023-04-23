package com.grad.user.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityRegisterBinding;
import com.grad.information.MainPageActivity;
import com.grad.pojo.User;
import com.grad.service.UserService;
import com.grad.user.commitee.CommiteeActivity;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;
import com.grad.util.StringTool;

import cn.hutool.core.util.StrUtil;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding mBinding;
    private Handler mHandler;
    private long utype = UserConstants.UTYPE_USER;
    private User mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        checkUserExists();
        initView();
        initListener();
    }

    private  void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UserConstants.REGISTER_USER_FAILED:{
                        mBinding.registerProgressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case UserConstants.REGISTER_USER_OK:{
                        mBinding.registerProgressbar.setVisibility(View.INVISIBLE);
                        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
                        sharedPreferenceUtil.writeString(UserConstants.SHARED_PREF_USERINFO_KEY, (String)msg.obj);
                        if(utype == UserConstants.UTYPE_USER){
                            startActivity(new Intent(RegisterActivity.this, MainPageActivity.class));
                        }else{
                            startActivity(new Intent(RegisterActivity.this, CommiteeActivity.class));
                        }
                        finish();
                        break;
                    }
                    case UserConstants.CHECK_USER_OK:{
                        Log.e("wjj", "handler msg:" + (String)msg.obj);
                        if(((String)msg.obj).equals(UserConstants.USER_EXISTS)) {
                            if(mUser.getUtype() == UserConstants.UTYPE_USER){
                                startActivity(new Intent(RegisterActivity.this, MainPageActivity.class));
                            }else{
                                startActivity(new Intent(RegisterActivity.this, CommiteeActivity.class));
                            }
                            finish();
                        }
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void checkUserExists(){
        String userStr = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE).readString(UserConstants.SHARED_PREF_USERINFO_KEY, null);
        if(userStr != null){
            mUser = JsonUtil.jsonToObject(userStr, User.class);
            UserService.checkIfUserExists(mHandler, mUser.getUid());
        }
    }

    private void initView(){
        SpannableString spannableString = new SpannableString(mBinding.tvLogin.getText());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        mBinding.tvLogin.setText(spannableString);

    }
    private void initListener(){
        mBinding.gbGwh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) utype = UserConstants.UTYPE_GWH;
                else utype = UserConstants.UTYPE_USER;
            }
        });

        mBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mBinding.email.getText().toString();
                String username = mBinding.username.getText().toString();
                String password = mBinding.password.getText().toString();
                if(email.length() > 50) {
                    Toast.makeText(getApplicationContext(), "邮箱长度要小于50", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!StringTool.isEmail(email)) {
                    Toast.makeText(getApplicationContext(), "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StrUtil.isEmpty(username) || StrUtil.isEmpty(password) || StrUtil.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "请将信息补充完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBinding.registerProgressbar.setVisibility(View.VISIBLE);
                User user = new User(null, utype,
                        username, password, email,
                        null, null,
                        null, 0, null);
                UserService.registerUser(mHandler, user);
            }
        });

        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}