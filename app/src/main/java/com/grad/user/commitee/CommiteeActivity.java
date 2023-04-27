package com.grad.user.commitee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityCommiteeBinding;
import com.grad.information.news.AddNewsActivity;
import com.grad.information.note.NoteItem;
import com.grad.pojo.User;
import com.grad.service.CommitteeService;
import com.grad.user.member.RegisterActivity;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import cn.hutool.core.util.StrUtil;

public class CommiteeActivity extends AppCompatActivity {

    ActivityCommiteeBinding mBinding;
    private Handler mHandler;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCommiteeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initData();
        initView();
        initListener();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case CommitteeConstants.UPLOAD_NOTE_OK:{
                        mBinding.etNote.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        Toast.makeText(getApplicationContext(), "发布成功！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case CommitteeConstants.UPLOAD_NOTE_FAILED:{
                        Toast.makeText(getApplicationContext(), "网络错误！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void initData(){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
        String strUser = sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, null);
        mUser = JsonUtil.jsonToObject(strUser, User.class);
    }

    private void initView(){
        mBinding.news.tv.setText("发布新闻");
        mBinding.report.tv.setText("投诉处理");
        mBinding.tvTop.setText(mUser.getCommunityName() + "居委会中心");
    }

    private void initListener(){
        mBinding.news.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommiteeActivity.this, AddNewsActivity.class));
            }
        });

        mBinding.report.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mBinding.btUploadNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mBinding.etNote.getText().toString();
                if(StrUtil.isEmpty(content)){
                    Toast.makeText(CommiteeActivity.this, "公告不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String communityName = mUser.getCommunityName();
                CommitteeService.uploadNote(mHandler,communityName, content);
            }
        });

        mBinding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
                sharedPreferenceUtil.deleteString(UserConstants.SHARED_PREF_USERINFO_KEY);
                startActivity(new Intent(CommiteeActivity.this, RegisterActivity.class));
                finish();
            }
        });


    }
}