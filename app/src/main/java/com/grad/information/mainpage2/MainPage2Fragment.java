package com.grad.information.mainpage2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.databinding.FragmentMainPage2Binding;
import com.grad.databinding.FragmentMainPageBinding;
import com.grad.information.mainpage.CustomStaggeredGridLayoutManager;
import com.grad.information.mainpage.ItemAdapter;
import com.grad.information.mainpage.MainPageFragment;
import com.grad.pojo.PostItem;

import java.util.ArrayList;
import java.util.List;


public class MainPage2Fragment extends Fragment {
    private Context mContext;
    private FragmentMainPage2Binding mBinding;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    private List<NavigationData> mNavigationDataList = new ArrayList<>();
    private List<View> mNavigationGridViewList = new ArrayList<>();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public MainPage2Fragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                mBinding = FragmentMainPage2Binding.inflate(inflater, container, false);

                return mBinding.getRoot();
    }

    private void initHandler(){

    }
    private void initData(){
        String[] tags = new String[]{"无标签", "跑腿", "寻人寻物", "二手交易", "家电维修", "政策讨论", "新闻娱乐", "家庭琐事", "办事预约", "社区投票"};
        mNavigationDataList.add(new NavigationData(R.mipmap.wbq, tags[0]));
        mNavigationDataList.add(new NavigationData(R.mipmap.paotui, tags[1]));
        mNavigationDataList.add(new NavigationData(R.mipmap.xrxw, tags[2]));
        mNavigationDataList.add(new NavigationData(R.mipmap.esjy, tags[3]));
        mNavigationDataList.add(new NavigationData(R.mipmap.jdwx, tags[4]));
        mNavigationDataList.add(new NavigationData(R.mipmap.zctl, tags[5]));
        mNavigationDataList.add(new NavigationData(R.mipmap.xwyl, tags[6]));
        mNavigationDataList.add(new NavigationData(R.mipmap.jtss, tags[7]));
        mNavigationDataList.add(new NavigationData(R.mipmap.bsyy, tags[8]));
        mNavigationDataList.add(new NavigationData(R.mipmap.sqtp, tags[9]));

        int gridViewPageSize = (int) Math.ceil(mNavigationDataList.size() * 1.0 / UserConstants.NAVIGATION_GRIDVIEW_ITEM_CNT);
        for(int i = 0; i < gridViewPageSize; i ++){
            GridView gridView = (GridView) getLayoutInflater().inflate(R.layout.navigation_grid_view, mBinding.nagViewPager, false);
            gridView.setAdapter(new NavigationGridViewAdapter(mContext, mNavigationDataList, i));
            mNavigationGridViewList.add(gridView);
        }
        NavigationViewPagerAdapter viewPagerAdapter = new NavigationViewPagerAdapter(mNavigationGridViewList);
        mBinding.nagViewPager.setAdapter(viewPagerAdapter);
        mBinding.dotsIndicator.setViewPager(mBinding.nagViewPager);
    }
    private void initView(){

    }
    private void initListener(){

    }

}