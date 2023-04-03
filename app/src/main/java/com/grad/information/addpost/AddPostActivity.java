package com.grad.information.addpost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.grad.R;
import com.grad.databinding.ActivityAddPostBinding;
import com.grad.http.DataSender;
import com.grad.pojo.Post;
import com.grad.pojo.User;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;
import com.grad.util.UriUtil;

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
                            DataSender.sendImages(mPostId, mHandler1, mSelectedImages.get(i), i);
                        }
                    }
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
                    Bitmap bitmap = UriUtil.getBitmapFromUri(imageUri, getApplicationContext());
                    String fileName = UriUtil.getFileNameFromUri(imageUri, getApplicationContext());
                    imageInfos.add(new ImageInfo(bitmap, fileName));
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                Bitmap bitmap = UriUtil.getBitmapFromUri(imageUri, getApplicationContext());
                String fileName = UriUtil.getFileNameFromUri(imageUri, getApplicationContext());
                imageInfos.add(new ImageInfo(bitmap, fileName));
            }

            addToImageHolder(imageInfos);
            mSelectedImages.addAll(imageInfos);
//            Log.e("wjj", "img count = " + mSelectedImages.size());

        }
    }


//    private Bitmap getBitmapFromUri(Uri uri) {
//        try {
//            ParcelFileDescriptor parcelFileDescriptor =
//                    getContentResolver().openFileDescriptor(uri, "r");
//            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//            parcelFileDescriptor.close();//
//            return bitmap;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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
//                    Log.e("wjj", "select index:" + mDeleteImgAt);
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
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), DefaultVals.USER_INFO_DATABASE);
        User user = JsonUtil.jsonToObject(sharedPreferenceUtil.readString("user", "null"), User.class);
        Post post = new Post("", user.getUid(), 0,
                mModelAddPost.getTitle(), mModelAddPost.getContent(),
                mModelAddPost.getTag(), 0, "");
        Log.e("wjj", "post Tag:" + post.getPostTag());

        DataSender.newPost(post, mHandler1);
    }



}