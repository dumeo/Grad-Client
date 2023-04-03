package com.grad.information.postdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.databinding.ItemCommentBinding;
import com.grad.pojo.CommentItem;
import com.grad.util.GlideUtil;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CommentItem> mCommentItems;
    private Context mContext;
    ItemCommentBinding mBinding;

    public CommentAdapter(Context mContext,List<CommentItem> comments) {
        this.mCommentItems = comments;
        this.mContext = mContext;
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
        TextView commentDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = mBinding.commentAvatar;
            userName = mBinding.commentUserName;
            userUnit = mBinding.commentUserUnit;
            content = mBinding.commentContent;
            likeCnt = mBinding.commentLikeCnt;
            commentDate = mBinding.commentDate;

        }
    }
}
