package com.grad.information.postdetail;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.databinding.ItemCommentBinding;
import com.grad.pojo.CommentItem;
import com.grad.service.CommentService;
import com.grad.constants.DefaultVals;
import com.grad.util.GlideUtil;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommentItem> mCommentItems;
    private Context mContext;
    ItemCommentBinding mBinding;
    private Handler handler;
    private String mClientUid;

    public CommentAdapter(Context mContext,List<CommentItem> comments, Handler handler) {
        this.mCommentItems = comments;
        this.mContext = mContext;
        this.handler = handler;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ItemCommentBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).userName.setText(mCommentItems.get(position).getUser().getUsername());
        ((MyViewHolder)holder).userUnit.setText(mCommentItems.get(position).getUser().getHouseAddr());
        ((MyViewHolder)holder).content.setText(mCommentItems.get(position).getComment().getContent());
        ((MyViewHolder)holder).likeCnt.setText("" + mCommentItems.get(position).getComment().getLikeCnt());
        ((MyViewHolder)holder).commentDate.setText(mCommentItems.get(position).getComment().getCommentDate());
        ShapeableImageView userAvatar = ((MyViewHolder)holder).userAvatar;
        GlideUtil.loadShapeableImageView(mContext,
                mCommentItems.get(position).getUser().getAvatarUrl(),
                userAvatar, GlideUtil.DefaultRequestOptions);
        int likeStatus = mCommentItems.get(position).getClientToThisInfo().getLikeStatus();
        switch (likeStatus){
            case DefaultVals.LIKE_STATUS_DISLIKED:{
                ((MyViewHolder)holder).downvote.setImageResource(R.mipmap.c_down_arrow);
                break;
            }
            case DefaultVals.LIKE_STATUS_LIKED:{
                ((MyViewHolder)holder).upvote.setImageResource(R.mipmap.c_up_arrow);
                break;
            }
        }

        if(mCommentItems.get(position).getChildComments().size() > 0){
            CommentAdapter childAdapter = new CommentAdapter(mContext, mCommentItems.get(position).getChildComments(), handler);
            childAdapter.setmClientUid(mClientUid);
            RecyclerView childRecyclerView = ((MyViewHolder)holder).rcChildComments;
            childRecyclerView.setAdapter(childAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            childRecyclerView.setLayoutManager(layoutManager);

        }

        ((MyViewHolder)holder).content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                CommentItem commentItem = mCommentItems.get(pos);
                Message message = Message.obtain();
                message.what = DefaultVals.REQUEST_ADD_COMMENT;
                message.obj = commentItem;
                handler.sendMessage(message);
            }
        });

        ((MyViewHolder)holder).upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                CommentItem commentItem = mCommentItems.get(pos);
                String commentId = mCommentItems.get(pos).getComment().getCommentId();
                int likeStatus = mCommentItems.get(pos).getClientToThisInfo().getLikeStatus();
                int likeCnt = (int) mCommentItems.get(pos).getComment().getLikeCnt();
                int transferType = -1;
                if(likeStatus == DefaultVals.LIKE_STATUS_LIKED){
                    transferType = DefaultVals.LIKED_TO_NOSTATUS;
                    ((MyViewHolder)holder).likeCnt.setText("" + (likeCnt - 1));
                    ((MyViewHolder)holder).upvote.setImageResource(R.mipmap.up_arrow);
                    commentItem.getClientToThisInfo().setLikeStatus(DefaultVals.LIKE_STATUS_NOSTATUS);
                    commentItem.getComment().setLikeCnt(likeCnt - 1);
                }

                else if(likeStatus == DefaultVals.LIKE_STATUS_DISLIKED){
                    transferType = DefaultVals.DISLIKED_TO_LIKE;
                    ((MyViewHolder)holder).likeCnt.setText("" + (likeCnt + 2));
                    ((MyViewHolder)holder).upvote.setImageResource(R.mipmap.c_up_arrow);
                    ((MyViewHolder)holder).downvote.setImageResource(R.mipmap.down_arrow);
                    mCommentItems.get(pos).getClientToThisInfo().setLikeStatus(DefaultVals.LIKE_STATUS_LIKED);
                    commentItem.getComment().setLikeCnt(likeCnt + 2);
                }

                else if(likeStatus == DefaultVals.LIKE_STATUS_NOSTATUS){
                    transferType = DefaultVals.NOSTATUS_TO_LIKE;
                    ((MyViewHolder)holder).likeCnt.setText("" + (likeCnt + 1));
                    ((MyViewHolder)holder).upvote.setImageResource(R.mipmap.c_up_arrow);
                    mCommentItems.get(pos).getClientToThisInfo().setLikeStatus(DefaultVals.LIKE_STATUS_LIKED);
                    commentItem.getComment().setLikeCnt(likeCnt + 1);
                }

                CommentService.setLikeStatus(handler, mClientUid, commentId, transferType);
            }
        });

        ((MyViewHolder)holder).downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                CommentItem commentItem = mCommentItems.get(pos);
                String commentId = mCommentItems.get(pos).getComment().getCommentId();
                int likeStatus = mCommentItems.get(pos).getClientToThisInfo().getLikeStatus();
                int likeCnt = (int) mCommentItems.get(pos).getComment().getLikeCnt();
                int transferType = -1;
                if(likeStatus == DefaultVals.LIKE_STATUS_LIKED){
                    transferType = DefaultVals.LIKED_TO_DISLIKE;
                    ((MyViewHolder)holder).likeCnt.setText("" + (likeCnt - 2));
                    ((MyViewHolder)holder).downvote.setImageResource(R.mipmap.c_down_arrow);
                    ((MyViewHolder)holder).upvote.setImageResource(R.mipmap.up_arrow);
                    commentItem.getClientToThisInfo().setLikeStatus(DefaultVals.LIKE_STATUS_DISLIKED);
                    commentItem.getComment().setLikeCnt(likeCnt - 2);
                }

                else if(likeStatus == DefaultVals.LIKE_STATUS_DISLIKED){
                    transferType = DefaultVals.DISLIKED_TO_NOSTATUS;
                    ((MyViewHolder)holder).likeCnt.setText("" + (likeCnt + 1));
                    ((MyViewHolder)holder).downvote.setImageResource(R.mipmap.down_arrow);
                    commentItem.getClientToThisInfo().setLikeStatus(DefaultVals.LIKE_STATUS_NOSTATUS);
                    commentItem.getComment().setLikeCnt(likeCnt + 1);
                }

                else if(likeStatus == DefaultVals.LIKE_STATUS_NOSTATUS){
                    transferType = DefaultVals.NOSTATUS_TO_DISLIKE;
                    ((MyViewHolder)holder).likeCnt.setText("" + (likeCnt - 1));
                    ((MyViewHolder)holder).downvote.setImageResource(R.mipmap.c_down_arrow);
                    commentItem.getClientToThisInfo().setLikeStatus(DefaultVals.LIKE_STATUS_DISLIKED);
                    commentItem.getComment().setLikeCnt(likeCnt - 1);
                }
                CommentService.setLikeStatus(handler, mClientUid, commentId, transferType);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mCommentItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView userAvatar;
        TextView userName;
        TextView userUnit;
        TextView content;
        TextView likeCnt;
        ImageView upvote;
        ImageView downvote;
        TextView commentDate;
        RecyclerView rcChildComments;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = mBinding.commentAvatar;
            userName = mBinding.commentUserName;
            userUnit = mBinding.commentUserUnit;
            content = mBinding.commentContent;
            likeCnt = mBinding.commentLikeCnt;
            commentDate = mBinding.commentDate;
            rcChildComments = mBinding.rvChildComments;
            upvote = mBinding.ivUpvote;
            downvote = mBinding.ivDownvote;
        }
    }

    public void setmClientUid(String mClientUid) {
        this.mClientUid = mClientUid;
    }
}
