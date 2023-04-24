package com.grad.information.reserve;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.os.Bundle;

import com.grad.R;
import com.grad.databinding.ActivityReserveBinding;
import com.grad.databinding.FragmentToReserveBinding;

import java.util.ArrayList;
import java.util.List;

public class ReserveActivity extends AppCompatActivity {
    private ActivityReserveBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityReserveBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void initView(){
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ToReserveFragment("办事预约"));
        fragmentList.add(new ReserveListFragment("我的预约"));
        mBinding.viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList));
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}