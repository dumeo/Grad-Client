package com.grad.information.mainpage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private TextView mTextViewMainPage;
    private TextView mTextViewRecommand;
    private TextView mTextViewFollowing;
    private TextView mTextViewProfile;
    private ShapeableImageView mNewPostImageView;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        return true;
    }




    private void initView(){
        mToolbar = findViewById(R.id.toolbar);
        mTextViewMainPage = findViewById(R.id.tv_main_page);
        mTextViewRecommand = findViewById(R.id.tv_recommand);
        mTextViewFollowing = findViewById(R.id.tv_following);
        mTextViewProfile = findViewById(R.id.tv_profile);
        mNewPostImageView = findViewById(R.id.iv_newpost);
        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        showFragment(transaction, new MainPageFragment());
        transaction.commit();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_news:{}
                    case R.id.action_second_hand:{}
                    case R.id.action_logout:{}
                    default:{}
                }
                return false;
            }
        });
    }

    public void showFragment(FragmentTransaction transaction, Fragment fragment){
        transaction.add(R.id.main_frames, fragment, "mainPageFragment");
        transaction.show(fragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_newpost:
            case R.id.tv_main_page:
            case R.id.tv_recommand:
            case R.id.tv_following:
            case R.id.tv_profile:
            default:
        }
    }
}