package com.grad.information.reserve;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public MyFragmentStatePagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragmentList) {
        super(fm, behavior);
        mFragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
            return ((ToReserveFragment)mFragmentList.get(position)).title;
        else return ((ReserveListFragment)mFragmentList.get(position)).title;
    }
}
