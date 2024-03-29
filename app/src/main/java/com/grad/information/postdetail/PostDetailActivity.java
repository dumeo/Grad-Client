package com.grad.information.postdetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grad.App;
import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityPostDetailBinding;
import com.grad.databinding.LayoutBanUserBinding;
import com.grad.information.infocategory.ItemSpaceDecoration;
import com.grad.pojo.ClientToThisInfo;
import com.grad.pojo.Comment;
import com.grad.pojo.CommentItem;
import com.grad.pojo.PostItem;
import com.grad.pojo.Status;
import com.grad.pojo.User;
import com.grad.service.CollectService;
import com.grad.service.CommentService;
import com.grad.service.CommitteeService;
import com.grad.service.PostService;
import com.grad.constants.DefaultVals;
import com.grad.service.UserService;
import com.grad.user.commitee.CommiteeActivity;
import com.grad.util.DialogUtil;
import com.grad.util.GlideUtil;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.StrUtil;

public class PostDetailActivity extends AppCompatActivity {
    ActivityPostDetailBinding mBinding;
    private String mPostId;
    private PostItem mPostItem;
    Handler mHandler;
    Handler mCommentHandler;
    private User mUser;
    MyViewPagerAdapter mViewPagerAdapter;
    List<CommentItem> mCommentItems = new ArrayList<>();
    private CommentAdapter mCommentAdapter;//
    private boolean mIsReplyToChildComment = false;
    private CommentItem mReplyToCommentItem;
    private int mLikeStatus = -1;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(mBinding.popUpComment.getVisibility() == View.VISIBLE){
            mBinding.popUpComment.setVisibility(View.INVISIBLE);
            mBinding.etComment.setText("");
        }
        else finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mPostId = getIntent().getStringExtra("postId");
        Log.e("wjj", "postId = " + mPostId);
        initHandler();
        initData();
        initListener();
    }


    private void initHandler(){
        mCommentHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case DefaultVals.LOAD_COMMENTS_SUCCESS:{
                        displayComments();
                        break;
                    }
                    case DefaultVals.ADD_COMMENT_SUCCESS:{
                        //after addding comment,reload comments
                        mCommentItems.clear();
                        mBinding.rcComments.getAdapter().notifyDataSetChanged();
                        CommentService.reloadCommentsByPostId(mCommentHandler, mUser.getUid(), mPostId, mCommentItems);
                        //update comment cnt
                        PostService.getCommentCnt(mHandler, mPostId);
                        break;
                    }
                    case DefaultVals.ADD_COMMENT_FAILED:{
                        Toast.makeText(PostDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                        UserService.checkUserBanned(mHandler, App.getUser().getEmail());
                        break;
                    }
                    case DefaultVals.REQUEST_ADD_COMMENT:{
                        mReplyToCommentItem = (CommentItem) msg.obj;
                        mBinding.tvComment.callOnClick();
                        mBinding.etComment.setHint("回复：\"" + mReplyToCommentItem.getComment().getContent() + "\"");
                        mIsReplyToChildComment = true;
                        break;
                    }

                    case DefaultVals.RELOAD_COMMENTS_SUCCESS:{
                        mBinding.rcComments.getAdapter().notifyDataSetChanged();
                    }

                    case DefaultVals.SET_LIKE_STATUS_SUCCESS:{
//                        mBinding.rcComments.getAdapter().notifyDataSetChanged();
                        break;
                    }
                    case DefaultVals.SET_LIKE_STATUS_FAILED:{
                        Toast.makeText(PostDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case UserConstants.CHECK_USER_BANNED_OK:{

                        break;
                    }
                }
                return false;
            }
        });

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case DefaultVals.GET_POST_SUCCESS:{
                        mPostItem = (PostItem) msg.obj;
                        if(mPostItem.getPost().getPostType() == DefaultVals.POST_TYPE_TEXT){
                            mBinding.viewPager.setLayoutParams(new RelativeLayout.LayoutParams(
                                    0, 0
                            ));
                            mBinding.dotsIndicator.setLayoutParams(new RelativeLayout.LayoutParams(
                                    0, 0
                            ));
                            mBinding.viewPager.setVisibility(View.INVISIBLE);
                            mBinding.dotsIndicator.setVisibility(View.INVISIBLE);
                        }

                        else{
                            mViewPagerAdapter = new MyViewPagerAdapter(PostDetailActivity.this, mPostItem);
                            mBinding.viewPager.setAdapter(mViewPagerAdapter);
                            mBinding.dotsIndicator.setViewPager(mBinding.viewPager);
                        }
                        //After fetching post, init view
                        initView();
                        //Load comments
                        CommentService.getCommentsByPostId( mCommentHandler, mUser.getUid(), mPostId, mCommentItems);
                        break;
                    }

                    case DefaultVals.GET_COMMENTCNT_SUCCESS:{
                        long cnt = (long) msg.obj;
                        mBinding.commentCnt.setText("" + cnt);
                        break;
                    }

                    case DefaultVals.SET_LIKE_STATUS_SUCCESS:{
                        long likeCnt = Integer.parseInt(mBinding.likeCnt.getText().toString());
                        int transferType = (int)msg.obj;
                        ClientToThisInfo clientToThisInfo = mPostItem.getClientToThisInfo();
                        if(transferType == DefaultVals.LIKED_TO_DISLIKE){
                            Log.e("wjj", "LIKED_TO_DISLIKE");
                            mBinding.like.setImageResource(R.mipmap.thumb_up);
                            mBinding.dislike.setImageResource(R.mipmap.c_thumb_down);
                            mBinding.likeCnt.setText("" + (likeCnt - 2));
                            clientToThisInfo.setLikeStatus(DefaultVals.LIKE_STATUS_DISLIKED);
                        }
                        else if(transferType == DefaultVals.DISLIKED_TO_LIKE){
                            Log.e("wjj", "DISLIKED_TO_LIKE");
                            mBinding.dislike.setImageResource(R.mipmap.thumb_down);
                            mBinding.like.setImageResource(R.mipmap.c_thumb_up);
                            mBinding.likeCnt.setText("" + (likeCnt + 2));
                            clientToThisInfo.setLikeStatus(DefaultVals.LIKE_STATUS_LIKED);
                        }
                        else if(transferType == DefaultVals.LIKED_TO_NOSTATUS){
                            Log.e("wjj", "LIKED_TO_NOSTATUS");
                            mBinding.like.setImageResource(R.mipmap.thumb_up);
                            mBinding.likeCnt.setText("" + (likeCnt - 1));
                            clientToThisInfo.setLikeStatus(DefaultVals.LIKE_STATUS_NOSTATUS);
                        }
                        else if(transferType == DefaultVals.DISLIKED_TO_NOSTATUS){
                            Log.e("wjj", "DISLIKED_TO_NOSTATUS");
                            mBinding.dislike.setImageResource(R.mipmap.thumb_down);
                            mBinding.likeCnt.setText("" + (likeCnt + 1));
                            clientToThisInfo.setLikeStatus(DefaultVals.LIKE_STATUS_NOSTATUS);
                        }
                        else if(transferType == DefaultVals.NOSTATUS_TO_DISLIKE){
                            Log.e("wjj", "NOSTATUS_TO_DISLIKE");
                            mBinding.dislike.setImageResource(R.mipmap.c_thumb_down);
                            mBinding.likeCnt.setText("" + (likeCnt - 1));
                            clientToThisInfo.setLikeStatus(DefaultVals.LIKE_STATUS_DISLIKED);
                        }
                        else if(transferType == DefaultVals.NOSTATUS_TO_LIKE){
                            Log.e("wjj", "NOSTATUS_TO_LIKE");
                            mBinding.like.setImageResource(R.mipmap.c_thumb_up);
                            mBinding.likeCnt.setText("" + (likeCnt + 1));
                            clientToThisInfo.setLikeStatus(DefaultVals.LIKE_STATUS_LIKED);
                        }
                        break;
                    }
                    case DefaultVals.ADD_COLLECT_SUCCESS:{
                        if(mPostItem.getClientToThisInfo().isCollected()){
                            mBinding.collect.setImageResource(R.mipmap.star);
                            mPostItem.getClientToThisInfo().setCollected(false);
                        }
                        else{
                            mBinding.collect.setImageResource(R.mipmap.stared);
                            mPostItem.getClientToThisInfo().setCollected(true);
                        }
                        break;
                    }

                    case UserConstants.CHECK_USER_BANNED_OK:{
                        Status status = JsonUtil.jsonToObject(msg.obj.toString(), Status.class);
                        if(status.getStatus().equals(UserConstants.USER_BANNED)){
                            String startDate = StrUtil.split(status.getMsg(), '=').get(0);
                            String days = StrUtil.split(status.getMsg(), '=').get(1);
                            String message = "您已被管理员禁言，从" + startDate + "开始，时长" + days + "天";
                            DialogUtil.showInfoDialog(PostDetailActivity.this, message);
                        }
                        break;
                    }

                }
                return false;
            }
        });



    }

    private void initView(){
        GlideUtil.loadShapeableImageView(PostDetailActivity.this, mUser.getAvatarUrl(),
                mBinding.userAvatar, GlideUtil.DefaultRequestOptions);
        mBinding.userName.setText(mPostItem.getUser().getUsername());
        mBinding.houseAddr.setText(mPostItem.getUser().getHouseAddr());
        mBinding.title.setText(mPostItem.getPost().getPostTitle());
        mBinding.content.setText(mPostItem.getPost().getPostContent());
        mBinding.postTime.setText(mPostItem.getPost().getPostDate());
        mBinding.likeCnt.setText("" + mPostItem.getPost().getLikeCnt());
        mBinding.commentCnt.setText("" + mPostItem.getPostInfo().getCommentCnt());
        mLikeStatus = mPostItem.getClientToThisInfo().getLikeStatus();
        if(mLikeStatus == DefaultVals.LIKE_STATUS_LIKED){
            mBinding.like.setImageResource(R.mipmap.c_thumb_up);
        }else if(mLikeStatus == DefaultVals.LIKE_STATUS_DISLIKED){
            mBinding.dislike.setImageResource(R.mipmap.c_thumb_down);
        }
        if(mPostItem.getClientToThisInfo().isCollected()){
            mBinding.collect.setImageResource(R.mipmap.stared);
        }
    }


    private  void initListener(){
        mBinding.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPostItem.getClientToThisInfo().isCollected()){
                    CollectService.addCollect(mHandler, mUser.getUid(), mPostId, DefaultVals.UNCOLLECT_POST);
                }
                else{
                    CollectService.addCollect(mHandler, mUser.getUid(), mPostId, DefaultVals.COLLECT_POST);
                }
            }
        });
        mBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int transferType = -1;
                int likeStatus = mPostItem.getClientToThisInfo().getLikeStatus();
                if(likeStatus == DefaultVals.LIKE_STATUS_LIKED)
                    transferType = DefaultVals.LIKED_TO_NOSTATUS;
                else if(likeStatus == DefaultVals.LIKE_STATUS_DISLIKED)
                    transferType = DefaultVals.DISLIKED_TO_LIKE;
                else if(likeStatus == DefaultVals.LIKE_STATUS_NOSTATUS)
                    transferType = DefaultVals.NOSTATUS_TO_LIKE;
                PostService.setLikeStatus(mHandler, mUser.getUid(), mPostId, transferType);
            }
        });

        mBinding.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int transferType = -1;
                int likeStatus = mPostItem.getClientToThisInfo().getLikeStatus();
                if(likeStatus == DefaultVals.LIKE_STATUS_LIKED)
                    transferType = DefaultVals.LIKED_TO_DISLIKE;
                else if(likeStatus == DefaultVals.LIKE_STATUS_DISLIKED)
                    transferType = DefaultVals.DISLIKED_TO_NOSTATUS;
                else if(likeStatus == DefaultVals.LIKE_STATUS_NOSTATUS)
                    transferType = DefaultVals.NOSTATUS_TO_DISLIKE;
                PostService.setLikeStatus(mHandler, mUser.getUid(), mPostId, transferType);
            }
        });


        mBinding.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.popUpComment.setVisibility(View.VISIBLE);
                mBinding.etComment.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(mBinding.etComment, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        mBinding.btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mBinding.etComment.getText().toString();
                if(content == null || content.equals("")) return;
                Comment comment = new Comment(
                        null, mUser.getUid(), mPostId,
                        content, 0, null, 0, null
                );

                //判断是否是回复某条评论
                if(mIsReplyToChildComment && mReplyToCommentItem != null){
                    //若是，则该评论层级加1
                    comment.setCommentLevel(mReplyToCommentItem.getComment().getCommentLevel()+1);
                    //标记父评论ID
                    comment.setFatherId(mReplyToCommentItem.getComment().getCommentId());
                    mIsReplyToChildComment = false;
                }
                mBinding.popUpComment.setVisibility(View.INVISIBLE);
                mBinding.etComment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                mBinding.etComment.setHint("在此评论...");
                //send comment...
                CommentService.sendComment(mCommentHandler, comment);

            }
        });

        mBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
        mUser = JsonUtil.jsonToObject(sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, "null"), User.class);
        //Get post
        PostService.getPostByid(mHandler, mUser.getUid(), mPostId);


    }


    private void displayComments(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(PostDetailActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBinding.rcComments.setLayoutManager(layoutManager);
        mCommentAdapter = new CommentAdapter(PostDetailActivity.this, mCommentItems, mCommentHandler);
        mCommentAdapter.setmClientUid(mUser.getUid());
        mBinding.rcComments.setAdapter(mCommentAdapter);
        mBinding.rcComments.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mBinding.rcComments.addItemDecoration(decoration);

    }


}