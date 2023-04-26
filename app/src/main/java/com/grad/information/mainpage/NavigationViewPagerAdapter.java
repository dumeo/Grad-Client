package com.grad.information.mainpage;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class NavigationViewPagerAdapter extends PagerAdapter {

    List<View> mGridViewList;

    public NavigationViewPagerAdapter(List<View> gridViews) {
        this.mGridViewList = gridViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mGridViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mGridViewList.get(position));
        return (mGridViewList.get(position));
    }

    @Override
    public int getCount() {
        if(mGridViewList == null) return 0;
        return mGridViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
