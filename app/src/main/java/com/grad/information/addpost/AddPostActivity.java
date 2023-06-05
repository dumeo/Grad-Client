package com.grad.information.addpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.grad.App;
import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityAddPostBinding;
import com.grad.databinding.LayoutInfoDialogBinding;
import com.grad.information.postdetail.PostDetailActivity;
import com.grad.pojo.Post;
import com.grad.pojo.Status;
import com.grad.pojo.User;
import com.grad.service.ImageService;
import com.grad.service.PostService;
import com.grad.constants.DefaultVals;
import com.grad.service.UserService;
import com.grad.util.DialogUtil;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;
import com.grad.util.UriUtil;
import com.grad.util.UriUtil2;

import java.util.ArrayList;
import java.util.List;


import cn.hutool.core.util.StrUtil;


public class AddPostActivity extends AppCompatActivity {
    ActivityAddPostBinding mBinding;
    private ViewModelAddPost mModelAddPost;
    private Handler mHandler1;
    private String mPostId;
    private final int PICK_IMAGE_REQUEST = 1;
    private List<ImageInfo> mSelectedImages = new ArrayList<>();
    private int mDeleteImgAt = -1;

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
        mHandler1 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if(msg.what == DefaultVals.ADD_POST_TEXT_SUCCESS){
                    mPostId = (String) msg.obj;
                    //如果有图片，上传图片
                    if(mSelectedImages.size() > 0){
                        for(int i = 0;i < mSelectedImages.size(); i ++){
                            ImageService.sendImages(mPostId, mHandler1, mSelectedImages.get(i), i);
                        }
                    }
                    finish();
                }
                else if(msg.what == DefaultVals.ADD_POST_SUCCESS){
                    finish();
                }
                else if(msg.what == UserConstants.CHECK_USER_BANNED_OK){
                    Status status = JsonUtil.jsonToObject(msg.obj.toString(), Status.class);
                    if(status.getStatus().equals(UserConstants.USER_BANNED)){
                        String startDate = StrUtil.split(status.getMsg(), '=').get(0);
                        String days = StrUtil.split(status.getMsg(), '=').get(1);
                        String message = "您已被管理员禁言，从" + startDate + "开始，时长" + days + "天";
                        // 创建一个AlertDialog.Builder对象
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddPostActivity.this);
                        builder.setTitle("注意");

                        // 防止内存泄漏
                        final com.grad.databinding.LayoutInfoDialogBinding[] binding = {LayoutInfoDialogBinding.inflate(getLayoutInflater(), null, false)};
                        builder.setView(binding[0].getRoot());
                        binding[0].tvMsg.setText(message);

                        // 设置对话框的“确定”按钮点击事件
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                binding[0] = null;
                                finish();
                            }
                        });

                        // 设置对话框的“取消”按钮点击事件
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 防止内存泄漏
                                binding[0] = null;
                                finish();
                            }
                        });
                        // 显示对话框
                        builder.show();
                    }
                }
                return false;
            }
        });

        if(mModelAddPost == null){
            mModelAddPost = new ViewModelAddPost();
            mBinding.setAddPostViewModel(mModelAddPost);
            mBinding.spinnerTag.setOnItemSelectedListener(mModelAddPost.mOnItemSelectedListener);
        }
        mBinding.setLifecycleOwner(this);

        UserService.checkUserBanned(mHandler1, App.getUser().getEmail());
    }

    private void initListener(){

        mBinding.btAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StrUtil.isEmpty(mModelAddPost.getTitle()) || StrUtil.isEmpty(mModelAddPost.getContent())){
                    Toast.makeText(getBaseContext(), "请补充标题或内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPost();
            }

        });

        mBinding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

                //---------------------------

            }
        });

        mBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.deleteImg.setClickable(false);
                mBinding.deleteImg.setVisibility(View.INVISIBLE);
                mBinding.cancel.setVisibility(View.INVISIBLE);
                mBinding.cancel.setClickable(false);
                mBinding.imgeHolder.getChildAt(mDeleteImgAt + 1)
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                mDeleteImgAt = -1;
            }
        });

        mBinding.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mDeleteImgAt != -1) {
                    mSelectedImages.remove(mDeleteImgAt);
                    mDeleteImgAt = -1;
                    mBinding.deleteImg.setClickable(false);
                    mBinding.deleteImg.setVisibility(View.INVISIBLE);
                    mBinding.cancel.setVisibility(View.INVISIBLE);
                    mBinding.cancel.setClickable(false);
                    updateImageLayout();
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<ImageInfo> imageInfos = new ArrayList<>();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    Log.e("wjj", "get FileName:" + UriUtil.getRealPathFromUri(getApplicationContext(), imageUri));
                    Bitmap bitmap = UriUtil.getBitmapFromUri(imageUri, getApplicationContext());
                    String fileName = UriUtil.getFileNameFromUri(imageUri, getApplicationContext());
                    imageInfos.add(new ImageInfo(bitmap, fileName));
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                Bitmap bitmap = UriUtil.getBitmapFromUri(imageUri, getApplicationContext());
                String fileName = UriUtil.getFileNameFromUri(imageUri, getApplicationContext());
                Log.e("wjj", UriUtil.getRealPathFromUri(getApplicationContext(), imageUri));
                imageInfos.add(new ImageInfo(bitmap, fileName));
            }

            addToImageHolder(imageInfos);
            mSelectedImages.addAll(imageInfos);
//            Log.e("wjj", "img count = " + mSelectedImages.size());

        }
    }



    private void addToImageHolder(List<ImageInfo> imageInfos){
        for (ImageInfo imageInfo : imageInfos) {
            MyImageView imageView = new MyImageView(this);
            imageView.setImageBitmap(imageInfo.getBitmap());
            imageView.setPos(mBinding.imgeHolder.getChildCount() - 1);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    imageView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.them_color));
                    mDeleteImgAt = imageView.getPos();
                    mBinding.cancel.setClickable(true);
                    mBinding.cancel.setVisibility(View.VISIBLE);
                    mBinding.deleteImg.setClickable(true);
                    mBinding.deleteImg.setVisibility(View.VISIBLE);
                    return false;
                }
            });


            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.img_preview_width),
                            ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(16, 0, 16, 0);
            imageView.setLayoutParams(layoutParams);
            mBinding.imgeHolder.addView(imageView);
        }
    }

    private void updateImageLayout() {
        mBinding.imgeHolder.removeAllViews();;
        mBinding.imgeHolder.addView(mBinding.addImage);
        addToImageHolder(mSelectedImages);
    }

    private void sendPost(){
        mBinding.progressBar.setVisibility(View.VISIBLE);
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
        User user = JsonUtil.jsonToObject(sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, "null"), User.class);
        //客户端构造信息实体
        Post post = new Post("", user.getUid(), 0,
                mModelAddPost.getTitle(), mModelAddPost.getContent(),
                mModelAddPost.getTag(), 0, "");
        //将信息实体传入服务端
        PostService.newPost(post, mHandler1);
    }



}