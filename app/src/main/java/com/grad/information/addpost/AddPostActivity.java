package com.grad.information.addpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.grad.databinding.ActivityAddPostBinding;
import com.grad.pojo.Post;
import com.grad.pojo.User;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.StrUtil;


public class AddPostActivity extends AppCompatActivity {
    ActivityAddPostBinding mBinding;
    private ViewModelAddPost mModelAddPost;
    private Handler mHandler1;
    private String mPostId;
    private final int PICK_IMAGE_REQUEST = 1;
    private List<Bitmap> mSelectedImages = new ArrayList<>();

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
                    finish();
                }
                else if(msg.what == DefaultVals.ADD_POST_SUCCESS){
                    finish();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    Bitmap bitmap = getBitmapFromUri(imageUri);
                    mSelectedImages.add(bitmap);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                Bitmap bitmap = getBitmapFromUri(imageUri);
                mSelectedImages.add(bitmap);
            }

            Log.e("wjj", "img count = " + mSelectedImages.size());

        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();//
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }




    private void sendPost(){
        mBinding.progressBar.setVisibility(View.VISIBLE);
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), DefaultVals.USER_INFO_DATABASE);
        User user = JsonUtil.jsonToObject(sharedPreferenceUtil.readString("user", "null"), User.class);
        Post post = new Post("", user.getUid(), 0,
                mModelAddPost.getTitle(), mModelAddPost.getContent(),
                mModelAddPost.getTag(), 0, "");
        DataSender.newPost(post, mHandler1);
    }





}