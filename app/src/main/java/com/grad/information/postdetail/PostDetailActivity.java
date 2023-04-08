package com.grad.information.postdetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.grad.R;
import com.grad.databinding.ActivityPostDetailBinding;
import com.grad.information.mainpage.ItemSpaceDecoration;
import com.grad.pojo.Comment;
import com.grad.pojo.CommentItem;
import com.grad.pojo.PostItem;
import com.grad.pojo.User;
import com.grad.service.CommentService;
import com.grad.service.PostService;
import com.grad.util.DefaultVals;
import com.grad.util.GlideUtil;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

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
                        CommentService.reloadCommentsByPostId(mCommentHandler, mUser.getUid(), mPostId, mCommentItems);
                        //update comment cnt
                        PostService.getCommentCnt(mHandler, mPostId);
                        break;
                    }
                    case DefaultVals.ADD_COMMENT_FAILED:{
                        Toast.makeText(PostDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
                        if(transferType == DefaultVals.LIKED_TO_DISLIKE){
                            mBinding.like.setImageResource(R.mipmap.thumb_up);
                            mBinding.dislike.setImageResource(R.mipmap.c_thumb_down);
                            mBinding.likeCnt.setText("" + (likeCnt - 2));
                            mLikeStatus = DefaultVals.LIKE_STATUS_DISLIKED;
                        }
                        else if(transferType == DefaultVals.DISLIKED_TO_LIKE){
                            mBinding.dislike.setImageResource(R.mipmap.thumb_down);
                            mBinding.like.setImageResource(R.mipmap.c_thumb_up);
                            mBinding.likeCnt.setText("" + (likeCnt + 2));
                            mLikeStatus = DefaultVals.LIKE_STATUS_LIKED;
                        }
                        else if(transferType == DefaultVals.LIKED_TO_NOSTATUS){
                            mBinding.like.setImageResource(R.mipmap.thumb_up);
                            mBinding.likeCnt.setText("" + (likeCnt - 1));
                            mLikeStatus = DefaultVals.LIKE_STATUS_NOSTATUS;
                        }
                        else if(transferType == DefaultVals.DISLIKED_TO_NOSTATUS){
                            mBinding.dislike.setImageResource(R.mipmap.thumb_down);
                            mBinding.likeCnt.setText("" + (likeCnt + 1));
                            mLikeStatus = DefaultVals.LIKE_STATUS_NOSTATUS;
                        }
                        else if(transferType == DefaultVals.NOSTATUS_TO_DISLIKE){
                            mBinding.dislike.setImageResource(R.mipmap.c_thumb_down);
                            mBinding.likeCnt.setText("" + (likeCnt - 1));
                            mLikeStatus = DefaultVals.LIKE_STATUS_DISLIKED;
                        }
                        else if(transferType == DefaultVals.NOSTATUS_TO_LIKE){
                            mBinding.like.setImageResource(R.mipmap.c_thumb_up);
                            mBinding.likeCnt.setText("" + (likeCnt + 1));
                            mLikeStatus = DefaultVals.LIKE_STATUS_LIKED;
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
        mBinding.userName.setText(mPostItem.getPostUserInfo().getUsername());
        mBinding.title.setText(mPostItem.getPost().getPostTitle());
        mBinding.content.setText(mPostItem.getPost().getPostContent());
        mBinding.postTime.setText(mPostItem.getPost().getPostDate());
        mBinding.likeCnt.setText("" + mPostItem.getPost().getLikeCnt());
        mBinding.commentCnt.setText("" + mPostItem.getPostInfo().getCommentCnt());
        mLikeStatus = mPostItem.getClientToThisInfo().getLikeStatus();
        if(mLikeStatus == DefaultVals.LIKE_STATUS_LIKED){
            mBinding.like.setImageResource(R.mipmap.c_thumb_up);
        }
        else if(mLikeStatus == DefaultVals.LIKE_STATUS_DISLIKED){
            mBinding.dislike.setImageResource(R.mipmap.c_thumb_down);
        }
    }


    private  void initListener(){
        mBinding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int transferType = -1;
                if(mLikeStatus == DefaultVals.LIKE_STATUS_LIKED)
                    transferType = DefaultVals.LIKED_TO_NOSTATUS;
                else if(mLikeStatus == DefaultVals.LIKE_STATUS_DISLIKED)
                    transferType = DefaultVals.DISLIKED_TO_LIKE;
                else if(mLikeStatus == DefaultVals.LIKE_STATUS_NOSTATUS)
                    transferType = DefaultVals.NOSTATUS_TO_LIKE;
                PostService.setLikeStatus(mHandler, mUser.getUid(), mPostId, transferType);
            }
        });

        mBinding.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int transferType = -1;
                if(mLikeStatus == DefaultVals.LIKE_STATUS_LIKED)
                    transferType = DefaultVals.LIKED_TO_DISLIKE;
                else if(mLikeStatus == DefaultVals.LIKE_STATUS_DISLIKED)
                    transferType = DefaultVals.DISLIKED_TO_NOSTATUS;
                else if(mLikeStatus == DefaultVals.LIKE_STATUS_NOSTATUS)
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
                if(mIsReplyToChildComment && mReplyToCommentItem != null){
                    comment.setCommentLevel(mReplyToCommentItem.getComment().getCommentLevel()+1);
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

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), DefaultVals.USER_INFO_DATABASE);
        mUser = JsonUtil.jsonToObject(sharedPreferenceUtil.readString("user", "null"), User.class);
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