package com.grad.user.commitee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityCommiteeBinding;
import com.grad.databinding.LayoutBanUserBinding;
import com.grad.databinding.LayoutInputLinkBinding;
import com.grad.information.delpost.DeletePostActivity;
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
                    case CommitteeConstants.BAN_USER_OK:{
                        Toast.makeText(getApplicationContext(), "操作成功！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case CommitteeConstants.BAN_USER_FAILED:{
                        Toast.makeText(getApplicationContext(), "操作失败！", Toast.LENGTH_SHORT).show();
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
        mBinding.userManage.tv.setText("禁言用户");
        mBinding.postManage.tv.setText("信息删除");
        mBinding.tvTop.setText(mUser.getCommunityName() + "居委会中心");
    }

    private void initListener(){
        mBinding.news.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommiteeActivity.this, AddNewsActivity.class));
            }
        });

        mBinding.userManage.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个AlertDialog.Builder对象
                AlertDialog.Builder builder = new AlertDialog.Builder(CommiteeActivity.this);
                builder.setTitle("输入用户信息");

                // 防止内存泄漏
                final com.grad.databinding.LayoutBanUserBinding[] binding = {LayoutBanUserBinding.inflate(getLayoutInflater(), null, false)};
                builder.setView(binding[0].getRoot());

                // 设置对话框的“确定”按钮点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = binding[0].etEmail.getText().toString();
                        int days = Integer.parseInt(binding[0].etTime.getText().toString());
                        CommitteeService.banUser(mHandler, email, days);
                        binding[0] = null;
                    }
                });

                // 设置对话框的“取消”按钮点击事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 防止内存泄漏
                        binding[0] = null;
                    }
                });

                // 显示对话框
                builder.show();
            }
        });

        mBinding.postManage.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommiteeActivity.this, DeletePostActivity.class));
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