package com.grad.information.recommand;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grad.R;
import com.grad.databinding.FragmentRecommandBinding;

public class RecommandFragment extends Fragment {
    private FragmentRecommandBinding mBinding;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    public RecommandFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRecommandBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    private void initHandler(){}
    private void initData(){}
    private void initView(){}
    private void initListener(){}
}