package com.grad.information.addpost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.grad.databinding.ActivityAddPostBinding;
import com.grad.pojo.User;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import cn.hutool.core.util.StrUtil;


public class AddPostActivity extends AppCompatActivity {
    ActivityAddPostBinding mBinding;
    private ViewModelAddPost mModelAddPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddPostBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);
        initView();
        initListener();
    }

    private void initView(){
        if(mModelAddPost == null){
            mModelAddPost = new ViewModelAddPost();
            mBinding.setAddPostViewModel(mModelAddPost);
            mBinding.spinnerTag.setOnItemSelectedListener(mModelAddPost.mOnItemSelectedListener);
        }
        mBinding.setLifecycleOwner(this);
    }

    private void initListener(){
        mBinding.btAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StrUtil.isEmpty(mModelAddPost.getTitle()) || StrUtil.isEmpty(mModelAddPost.getContent())){
                    Toast.makeText(getBaseContext(), "请补充标题或内容", Toast.LENGTH_SHORT).show();
                    return;
                }


                SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), DefaultVals.USER_INFO_DATABASE);
                User user = JsonUtil.jsonToObject(sharedPreferenceUtil.readString("user", "null"), User.class);

            }

        });
    }
}