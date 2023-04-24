package com.grad.information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.databinding.ActivityMainPageBinding;
import com.grad.information.addpost.AddPostActivity;
import com.grad.information.mainpage.MainPageFragment;
import com.grad.information.me.UserProfileFragment;
import com.grad.information.note.NoteListActivity;
import com.grad.information.recommand.RecommandFragment;
import com.grad.information.reserve.ReserveActivity;
import com.grad.information.vote.VoteListActivity;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private TextView mTextViewMainPage;
    private TextView mTextViewRecommand;
    private TextView mTextViewFollowing;
    private TextView mTextViewProfile;
    private ShapeableImageView mNewPostImageView;
    ActivityMainPageBinding mBinding;
    private final String mMainPageTag = "main page";
    private final String mProfilePageTag = "profile page";
    private final String mRecommendPageTag = "recommend page";

    private FragmentManager mFragmentManager;
    private MainPageFragment mMainPageFragment;
    private UserProfileFragment mUserProfileFragment;
    private RecommandFragment mRecommandFragment;
    Fragment mCurrentFragment;
    private boolean mIsFirstOpened = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initData();
        initView();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        return true;
    }



    private void initData(){
        mMainPageFragment = new MainPageFragment();
        mUserProfileFragment = new UserProfileFragment();
        mRecommandFragment = new RecommandFragment();
    }
    private void initView(){
        mToolbar = findViewById(R.id.toolbar);
        mTextViewMainPage = findViewById(R.id.tv_main_page);
        mTextViewRecommand = findViewById(R.id.tv_recommand);
        mTextViewFollowing = findViewById(R.id.tv_following);
        mTextViewProfile = findViewById(R.id.tv_profile);
        mNewPostImageView = findViewById(R.id.iv_addpost);
        mTextViewMainPage.setOnClickListener(this);
        mTextViewRecommand.setOnClickListener(this);
        mTextViewFollowing.setOnClickListener(this);
        mTextViewProfile.setOnClickListener(this);
        mNewPostImageView.setOnClickListener(this);

        mFragmentManager = getSupportFragmentManager();

        //增加首页fragment
        mFragmentManager.beginTransaction().add(R.id.main_frames, mMainPageFragment, mMainPageTag).commit();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_news:{
                        break;
                    }
                    case R.id.action_note:{
                        startActivity(new Intent(MainPageActivity.this, NoteListActivity.class));
                        break;
                    }
                    case R.id.action_reserve:{
                        startActivity(new Intent(MainPageActivity.this, ReserveActivity.class));
                        break;
                    }
                    case R.id.action_vote:{
                        startActivity(new Intent(MainPageActivity.this, VoteListActivity.class));
                        break;
                    }
                    default:{}
                }
                return false;
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_addpost:{
                startActivity(new Intent(this, AddPostActivity.class));
                break;
            }
            case R.id.tv_main_page:{
                clearSelectState();
                hideAllFragments();
                mTextViewMainPage.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
                Fragment mainPageFragment = getSupportFragmentManager().findFragmentByTag(mMainPageTag);
                if(mainPageFragment != null) {
                    getSupportFragmentManager().beginTransaction().show(mainPageFragment).commit();
                }
                break;
            }
            case R.id.tv_recommand:{
                clearSelectState();
                hideAllFragments();
                mTextViewRecommand.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
                Fragment recommendFragment = getSupportFragmentManager().findFragmentByTag(mRecommendPageTag);
                if(recommendFragment != null){
                    getSupportFragmentManager().beginTransaction().show(recommendFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().add(R.id.main_frames, mRecommandFragment, mRecommendPageTag).commit();
                }
                break;
            }
            case R.id.tv_following:{
                clearSelectState();
                mTextViewFollowing.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
                break;

            }
            case R.id.tv_profile:{
                clearSelectState();
                hideAllFragments();
                mTextViewProfile.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.black));
                Fragment profileFragment = getSupportFragmentManager().findFragmentByTag(mProfilePageTag);
                if(profileFragment != null){
                    getSupportFragmentManager().beginTransaction().show(profileFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().add(R.id.main_frames, mUserProfileFragment, mProfilePageTag).commit();
                }
                break;
            }
            default:{break;}
        }
    }

    private void hideAllFragments(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mMainPageTag);
        if(fragment != null && fragment.isAdded()) getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        fragment = getSupportFragmentManager().findFragmentByTag(mProfilePageTag);
        if(fragment != null && fragment.isAdded()) getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        fragment = getSupportFragmentManager().findFragmentByTag(mRecommendPageTag);
        if(fragment != null && fragment.isAdded()) getSupportFragmentManager().beginTransaction().hide(fragment).commit();

    }

    @SuppressLint("ResourceAsColor")
    private void clearSelectState(){
        mTextViewMainPage.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
        mTextViewRecommand.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
        mTextViewProfile.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
        mTextViewFollowing.setTextColor(ContextCompat.getColor(MainPageActivity.this, R.color.bottom_textcolor));
    }

}