package com.grad.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.grad.R;
import com.grad.databinding.ActivityRegisterBinding;
import com.grad.pojo.User;
import com.grad.user.UserViewModel;
import com.grad.util.DefaultVals;

public class RegisterActivity extends AppCompatActivity {
    private UserViewModel mUserViewModel;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mUserViewModel.getRegisterStatus().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer status) {
                if(mProgressBar == null) mProgressBar = findViewById(R.id.register_progressbar);
                if(status.equals(DefaultVals.UNDER_REGISTER) || status.equals(DefaultVals.REGISTERING_FAILED) || status.equals(DefaultVals.REGISTERING_SUCCESS))
                    mProgressBar.setVisibility(View.INVISIBLE);
                else if(status.equals(DefaultVals.REGISTERING))
                    mProgressBar.setVisibility(View.VISIBLE);
                else if(status.equals(DefaultVals.REGISTER_UNFILL))
                    Toast.makeText(getApplicationContext(), "请将信息补充完整", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initView(){
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
        if(mUserViewModel == null){
            mUserViewModel = new UserViewModel(new User());
            binding.setUserViewModel(mUserViewModel);
        }
        binding.setLifecycleOwner(this);
    }
}

