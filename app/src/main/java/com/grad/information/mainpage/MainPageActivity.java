package com.grad.information.mainpage;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.grad.App;
import com.grad.R;
import com.grad.constants.PostConstants;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityMainPageBinding;
import com.grad.information.addpost.AddPostActivity;
import com.grad.information.infocategory.InfoCategoryFragment;
import com.grad.information.me.UserProfileFragment;
import com.grad.information.note.NoteItem;
import com.grad.information.note.NoteListActivity;
import com.grad.information.recommand.RecommandFragment;
import com.grad.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity{
    ActivityMainPageBinding mBinding;
    private final String mProfilePageTag = "profile page";
    private final String mRecommendPageTag = "recommend page";
    private final String mInfoCategoryPageTag = "category info";
    private UserProfileFragment mUserProfileFragment;
    private RecommandFragment mRecommandFragment;
    private InfoCategoryFragment mInfoCategoryFragment;
    private Handler mHandler;
    private List<NavigationData> mNavigationDataList = new ArrayList<>();
    private List<View> mNavigationGridViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initData();
        initView();
        initListener();
    }
    @Override
    public void onBackPressed() {
        if(mBinding.rlMainPage.getVisibility() == View.VISIBLE)
            super.onBackPressed();
        else{
            mBinding.tvMainPage.callOnClick();
        }
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case PostConstants.SHOW_THESE_POST:{
                        String tag = (String) msg.obj;
                        clearSelectState();
                        hideAllFragments();
                        mBinding.mainFrames.setVisibility(View.VISIBLE);
                        mBinding.rlMainPage.setVisibility(View.INVISIBLE);
                        Log.e("wjj", "frame height:" + mBinding.mainFrames.getHeight());
                        Log.e("wjj", "rlMainpage height:" + mBinding.rlMainPage.getHeight());

                        Fragment infoCategoryFragment = getSupportFragmentManager().findFragmentByTag(mInfoCategoryPageTag);
                        if(infoCategoryFragment != null){
                            mInfoCategoryFragment.reloadPostsByTag(tag);
                            getSupportFragmentManager().beginTransaction().show(infoCategoryFragment).commit();
                        }else{
                            mInfoCategoryFragment = new InfoCategoryFragment(tag);
                            getSupportFragmentManager().beginTransaction().add(R.id.main_frames, mInfoCategoryFragment, mInfoCategoryPageTag).commit();
                        }
                        break;
                    }

                    case UserConstants.GET_NEWEST_NOTE_OK:{
                        NoteItem noteItem = (NoteItem) msg.obj;
                        mBinding.note.tvContent.setText(noteItem.getContent());
                        mBinding.note.tvInfo.setText(noteItem.getCommunityName() + " " + noteItem.getCreateDate());
                        mBinding.note.tvReadCnt.setText("");
                        break;
                    }
                }
                return false;
            }
        });
    }


    private void initData(){
        UserService.getNewestNote(mHandler, App.getUser().getCommunityName());
        mUserProfileFragment = new UserProfileFragment();
        mRecommandFragment = new RecommandFragment();
        //----------------------
        SpannableString spannableString = new SpannableString(mBinding.tvMoreNotes.getText().toString());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        mBinding.tvMoreNotes.setText(spannableString);


        mNavigationDataList.add(new NavigationData(R.mipmap.wbq, getResources().getString(R.string.tag_wbq)));
        mNavigationDataList.add(new NavigationData(R.mipmap.paotui,getResources().getString(R.string.tag_paotui)));
        mNavigationDataList.add(new NavigationData(R.mipmap.xrxw, getResources().getString(R.string.tag_xrxw)));
        mNavigationDataList.add(new NavigationData(R.mipmap.esjy, getResources().getString(R.string.tag_esjy)));
        mNavigationDataList.add(new NavigationData(R.mipmap.jdwx, getResources().getString(R.string.tag_jdwx)));
        mNavigationDataList.add(new NavigationData(R.mipmap.zctl, getResources().getString(R.string.tag_zctl)));
        mNavigationDataList.add(new NavigationData(R.mipmap.xwyl, getResources().getString(R.string.tag_xwyl)));
        mNavigationDataList.add(new NavigationData(R.mipmap.jtss, getResources().getString(R.string.tag_jtss)));
        mNavigationDataList.add(new NavigationData(R.mipmap.bsyy, getResources().getString(R.string.tag_bsyy)));
        mNavigationDataList.add(new NavigationData(R.mipmap.sqtp, getResources().getString(R.string.tag_sqtp)));

        int gridViewPageSize = (int) Math.ceil(mNavigationDataList.size() * 1.0 / UserConstants.NAVIGATION_GRIDVIEW_ITEM_CNT);
        for(int i = 0; i < gridViewPageSize; i ++){
            GridView gridView = (GridView) getLayoutInflater().inflate(R.layout.navigation_grid_view, mBinding.nagViewPager, false);
            gridView.setAdapter(new NavigationGridViewAdapter(mHandler, MainPageActivity.this, mNavigationDataList, i));
            mNavigationGridViewList.add(gridView);
        }
        NavigationViewPagerAdapter viewPagerAdapter = new NavigationViewPagerAdapter(mNavigationGridViewList);
        mBinding.nagViewPager.setAdapter(viewPagerAdapter);
        mBinding.dotsIndicator.setViewPager(mBinding.nagViewPager);
    }

    private void initView(){
        mBinding.tvCommunityName.setText(App.getUser().getCommunityName());
    }


    private void initListener(){
        mBinding.tvMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectState();
                hideAllFragments();
                mBinding.mainFrames.setVisibility(View.INVISIBLE);
                if(mBinding.rlMainPage.getVisibility() == View.INVISIBLE){
                    mBinding.rlMainPage.setVisibility(View.VISIBLE);
                }
                mBinding.tvMainPage.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
            }
        });

        mBinding.ivAddpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, AddPostActivity.class));
            }
        });

        mBinding.tvRecommand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectState();
                hideAllFragments();
                mBinding.mainFrames.setVisibility(View.VISIBLE);
                mBinding.rlMainPage.setVisibility(View.INVISIBLE);
                mBinding.tvRecommand.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
                Fragment recommendFragment = getSupportFragmentManager().findFragmentByTag(mRecommendPageTag);
                if(recommendFragment != null){
                    getSupportFragmentManager().beginTransaction().show(recommendFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().add(R.id.main_frames, mRecommandFragment, mRecommendPageTag).commit();
                }
            }
        });

        mBinding.tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectState();
                mBinding.tvFollowing.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
            }
        });

        mBinding.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectState();
                mBinding.rlMainPage.setVisibility(View.INVISIBLE);
                mBinding.mainFrames.setVisibility(View.VISIBLE);
                hideAllFragments();
                mBinding.tvProfile.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
                Fragment profileFragment = getSupportFragmentManager().findFragmentByTag(mProfilePageTag);
                if(profileFragment != null){
                    getSupportFragmentManager().beginTransaction().show(profileFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().add(R.id.main_frames, mUserProfileFragment, mProfilePageTag).commit();
                }
            }
        });

        mBinding.tvMoreNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, NoteListActivity.class));
            }
        });

        mBinding.note.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPageActivity.this, NoteListActivity.class));
            }
        });
    }


    private void hideAllFragments(){
        Fragment fragment;
        fragment = getSupportFragmentManager().findFragmentByTag(mProfilePageTag);
        if(fragment != null && fragment.isAdded()) getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        fragment = getSupportFragmentManager().findFragmentByTag(mRecommendPageTag);
        if(fragment != null && fragment.isAdded()) getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        fragment = getSupportFragmentManager().findFragmentByTag(mInfoCategoryPageTag);
        if(fragment != null && fragment.isAdded()) getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    @SuppressLint("ResourceAsColor")
    private void clearSelectState(){
        mBinding.tvMainPage.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
        mBinding.tvRecommand.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
        mBinding.tvProfile.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
        mBinding.tvFollowing.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
    }

}