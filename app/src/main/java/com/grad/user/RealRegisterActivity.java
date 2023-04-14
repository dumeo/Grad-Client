package com.grad.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.grad.R;
import com.grad.databinding.ActivityRealRegisterBinding;
import com.grad.databinding.ActivityRegisterBinding;

public class RealRegisterActivity extends AppCompatActivity {
    ActivityRealRegisterBinding mBinding;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRealRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private  void initHandler(){

    }
    private void initView(){

    }
    private void initListener(){
        mBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mBinding.email.getText().toString();
            }
        });
    }
}