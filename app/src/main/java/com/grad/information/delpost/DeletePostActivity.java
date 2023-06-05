package com.grad.information.delpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.grad.R;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.PostConstants;
import com.grad.databinding.ActivityDeletePostBinding;
import com.grad.databinding.LayoutInfoDialogBinding;
import com.grad.information.infocategory.ItemSpaceDecoration;
import com.grad.pojo.Post;
import com.grad.service.PostService;

import java.util.ArrayList;
import java.util.List;

public class DeletePostActivity extends AppCompatActivity {
    private Handler mHandler;
    private List<Post> mPostList = new ArrayList<>();
    private DeletePostAdapter mAdapter;
    private ActivityDeletePostBinding mBinding;
    private int mPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDeletePostBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initView();
        initListener();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case PostConstants.SEARCH_POSTS_OK:{
                        mAdapter.notifyDataSetChanged();
                        if(mPostList.size() == 0){
                            mBinding.tvNores.setVisibility(View.VISIBLE);
                            mBinding.rvRes.setVisibility(View.INVISIBLE);
                        }else{
                            mBinding.tvNores.setVisibility(View.INVISIBLE);
                            mBinding.rvRes.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                    case PostConstants.DELETE_THIS_POST:{
                        mPos = (int) msg.obj;
                        String postId = mPostList.get(mPos).getPostId();
                        // 创建一个AlertDialog.Builder对象
                        AlertDialog.Builder builder = new AlertDialog.Builder(DeletePostActivity.this);
                        builder.setTitle("注意");

                        // 防止内存泄漏
                        final com.grad.databinding.LayoutInfoDialogBinding[] binding = {LayoutInfoDialogBinding.inflate(LayoutInflater.from(DeletePostActivity.this), null, false)};
                        builder.setView(binding[0].getRoot());
                        binding[0].tvMsg.setText("确定要删除此信息？");

                        // 设置对话框的“确定”按钮点击事件
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PostService.deletePost(mHandler, postId);
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
                        break;
                    }

                    case PostConstants.DELETE_POST_OK:{
                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        mPostList.remove(mPos);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }

                    case PostConstants.DELETE_POST_FAILED:{
                        Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
                        break;
                    }

                }

                return false;
            }
        });
    }
    private void initData(){}
    private void initView(){
        mAdapter = new DeletePostAdapter(mHandler, mPostList, DeletePostActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DeletePostActivity.this);
        mBinding.rvRes.setLayoutManager(layoutManager);
        mBinding.rvRes.setPadding(5,5,5,5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mBinding.rvRes.addItemDecoration(decoration);
        mBinding.rvRes.setAdapter(mAdapter);

    }
    private void initListener(){
        mBinding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postTitle = mBinding.etSearch.getText().toString();
                mPostList.clear();
                mAdapter.notifyDataSetChanged();
                PostService.searchPost(mHandler, postTitle, mPostList);
            }
        });
    }
}