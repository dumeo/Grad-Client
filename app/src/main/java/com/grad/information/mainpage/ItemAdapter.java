package com.grad.information.mainpage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.databinding.ItemTypeImageBinding;
import com.grad.databinding.ItemTypeTextBinding;
import com.grad.pojo.PostItem;
import com.grad.util.DefaultVals;
import com.grad.util.GlideUtil;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostItem> mPostItems;
    private Context mContext;
    private ItemTypeImageBinding mItemTypeImageBinding;
    private ItemTypeTextBinding mItemTypeTextBinding;
    private RequestOptions mRequestOptions;

    public ItemAdapter(Context context, List<PostItem> postItems){
        mContext = context;
        this.mPostItems = postItems;
        mRequestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);
    }

    public List<PostItem> getmPostItems() {
        return mPostItems;
    }

    public void setmPostItems(List<PostItem> mPostItems) {
        this.mPostItems = mPostItems;
    }

    @Override
    public int getItemViewType(int position) {
        // 返回 View 布局类型, POST_TYPE_TEXT文字贴布局，POST_TYPE_IMG图片贴布局
        return (int) mPostItems.get(position).getPostType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mItemTypeImageBinding = ItemTypeImageBinding.inflate(LayoutInflater.from(mContext), parent, false);
        mItemTypeTextBinding = ItemTypeTextBinding.inflate(LayoutInflater.from(mContext), parent, false);

        if(viewType == DefaultVals.POST_TYPE_TEXT){
            return new ViewHolderWithContent( mItemTypeTextBinding.getRoot());
        }
        else{
            return new ViewHolderWithImg(mItemTypeImageBinding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == DefaultVals.POST_TYPE_TEXT){
//            ((ViewHolderWithContent)holder).userAvatar.setImageResource(R.drawable.ic_launcher_background);
            GlideUtil.loadShapeableImageView(mContext, mPostItems.get(position).getAvatarUrl(),
                    (ShapeableImageView) ((ViewHolderWithContent)holder).userAvatar,
                    mRequestOptions);

            ((ViewHolderWithContent)holder).username.setText(mPostItems.get(position).getUsername());
            ((ViewHolderWithContent)holder).userUnit.setText(mPostItems.get(position).getUserHouseAddr());
            ((ViewHolderWithContent)holder).postTitle.setText(mPostItems.get(position).getPostTitle());
            ((ViewHolderWithContent)holder).postContent.setText(mPostItems.get(position).getPostContent());

            if(mPostItems.get(position).getPostTag().equals("无标签"))
                ((ViewHolderWithContent)holder).postTag.setVisibility(View.INVISIBLE);
            else
                ((ViewHolderWithContent)holder).postTag.setText(mPostItems.get(position).getPostTag());

            ((ViewHolderWithContent)holder).postDate.setText(mPostItems.get(position).getPostDate());
        }

        else{
            GlideUtil.loadShapeableImageView(mContext, mPostItems.get(position).getAvatarUrl(),
                    (ShapeableImageView) ((ViewHolderWithImg)holder).userAvatar,
                    mRequestOptions);
            ((ViewHolderWithImg)holder).username.setText(mPostItems.get(position).getUsername());
            ((ViewHolderWithImg)holder).userUnit.setText(mPostItems.get(position).getUserHouseAddr());
            ((ViewHolderWithImg)holder).postTitle.setText(mPostItems.get(position).getPostTitle());
            GlideUtil.loadImageView(mContext, mPostItems.get(position).getMediaUrl().get(0),
                    (ImageView) ((ViewHolderWithImg)holder).postImage, mRequestOptions);

            if(mPostItems.get(position).getPostTag().equals("无标签"))
                ((ViewHolderWithImg)holder).postTag.setVisibility(View.INVISIBLE);
            else
                ((ViewHolderWithImg)holder).postTag.setText(mPostItems.get(position).getPostTag());

            ((ViewHolderWithImg)holder).postDate.setText(mPostItems.get(position).getPostDate());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPostItems.size();
    }

    class ViewHolderWithImg extends RecyclerView.ViewHolder{
        public ImageView userAvatar ;
        public TextView username ;
        public TextView userUnit ;
        public ImageView postImage;
        public TextView postTitle ;
        public TextView postTag ;
        public TextView postDate ;
        public ViewHolderWithImg(@NonNull View itemView) {
            super(itemView);
            userAvatar = mItemTypeImageBinding.userAvatar;
            username = mItemTypeImageBinding.username;
            userUnit = mItemTypeImageBinding.userUnit;
            postImage = mItemTypeImageBinding.postImage;
            postTitle = mItemTypeImageBinding.postTitle;
            postTag = mItemTypeImageBinding.postTag;
            postDate = mItemTypeImageBinding.postDate;
        }
    }

    class ViewHolderWithContent extends RecyclerView.ViewHolder{
        public ImageView userAvatar ;
        public TextView username ;
        public TextView userUnit ;
        public TextView postTitle ;
        public TextView postContent ;
        public TextView postTag ;
        public TextView postDate ;
        public ViewHolderWithContent(@NonNull View itemView) {
            super(itemView);

            userAvatar = mItemTypeTextBinding.userAvatar2;
            username = mItemTypeTextBinding.username2;
            userUnit = mItemTypeTextBinding.userUnit2;
            postTitle = mItemTypeTextBinding.postTitle2;
            postContent = mItemTypeTextBinding.postContent2;
            postTag = mItemTypeTextBinding.postTag2;
            postDate = mItemTypeTextBinding.postDate2;


        }
    }
}
