package com.grad.information.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grad.App;
import com.grad.constants.UserConstants;
import com.grad.databinding.FragmentUserProfileBinding;
import com.grad.user.member.RegisterActivity;
import com.grad.util.SharedPreferenceUtil;

public class UserProfileFragment extends Fragment {
    private Context mContext;
    private FragmentUserProfileBinding mBinding;
    private Handler mHandler;
    public UserProfileFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        initHandler();
        initDate();
        initView();
        initListener();
        return mBinding.getRoot();
    }

    private void initHandler(){

    }
    private void initDate(){

    }
    private void initView(){

    }

    private void initListener(){
        mBinding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.deleteUser(getActivity().getApplicationContext());
                startActivity(new Intent(mContext, RegisterActivity.class));
                getActivity().finish();
            }
        });
    }
}