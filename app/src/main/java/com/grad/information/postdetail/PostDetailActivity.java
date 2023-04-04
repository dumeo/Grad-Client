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
        Log.e("wjj", "get postId:" + mPostId);
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
                        Comment comment = (Comment) msg.obj;
                        CommentItem commentItem = new CommentItem(comment, mUser);
                        mCommentItems.add(0, commentItem);
                        mCommentAdapter.notifyDataSetChanged();
                        //after addding comment, update comment cnt
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
                        CommentService.getCommentsByPostId(mCommentHandler, mPostId, mCommentItems);
                        break;
                    }

                    case DefaultVals.GET_COMMENTCNT_SUCCESS:{
                        long cnt = (long) msg.obj;
                        mBinding.commentCnt.setText("" + cnt);
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
        PostService.getCommentCnt(mHandler, mPostId);
    }


    private  void initListener(){

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
                //send comment...
                CommentService.sendComment(mCommentHandler, comment);

            }
        });
    }

    private void initData(){

        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), DefaultVals.USER_INFO_DATABASE);
        mUser = JsonUtil.jsonToObject(sharedPreferenceUtil.readString("user", "null"), User.class);
        //Get post
        PostService.getPostByid(mHandler, mPostId);


    }


    private void displayComments(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(PostDetailActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mBinding.rcComments.setLayoutManager(layoutManager);
        mCommentAdapter = new CommentAdapter(PostDetailActivity.this, mCommentItems, mCommentHandler);
        mBinding.rcComments.setAdapter(mCommentAdapter);
        mBinding.rcComments.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mBinding.rcComments.addItemDecoration(decoration);

    }


}