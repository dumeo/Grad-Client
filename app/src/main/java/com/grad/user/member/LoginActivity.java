package com.grad.user.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityLoginBinding;
import com.grad.information.MainPageActivity;
import com.grad.service.UserService;
import com.grad.util.SharedPreferenceUtil;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding mBinding;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initView();
        initListener();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UserConstants.LOGIN_USER_OK:{
                        String strUser = (String) msg.obj;
                        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
                        sharedPreferenceUtil.writeString(UserConstants.SHARED_PREF_USERINFO_KEY, strUser);
                        startActivity(new Intent(LoginActivity.this, MainPageActivity.class));
                        finish();
                        break;
                    }
                    case UserConstants.LOGIN_USER_FAILED:{
                        Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void initView(){

    }

    private void initListener(){
        mBinding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mBinding.email.getText().toString();
                String password = mBinding.password.getText().toString();
                UserService.loginUser(mHandler, email, password);
            }
        });
    }
}