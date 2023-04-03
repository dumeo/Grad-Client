package com.grad.information.mainpage;

import android.content.Context;
import android.content.Intent;
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
import com.grad.information.postdetail.PostDetailActivity;
import com.grad.pojo.PostItem;
import com.grad.util.DefaultVals;
import com.grad.util.GlideUtil;
import com.grad.util.ImageUtil;

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
        return (int) mPostItems.get(position).getPost().getPostType();
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
        String userAvatarUrl = mPostItems.get(position).getPostUserInfo().getAvatarUrl();
        String username = mPostItems.get(position).getPostUserInfo().getUsername();
        String userHouseAddr = mPostItems.get(position).getPostUserInfo().getUserHouseAddr();
        String postTitle = mPostItems.get(position).getPost().getPostTitle();
        String postContent = mPostItems.get(position).getPost().getPostContent();
        String postTag = mPostItems.get(position).getPost().getPostTag();
        String postDate = mPostItems.get(position).getPost().getPostDate();

        if(getItemViewType(position) == DefaultVals.POST_TYPE_TEXT){
            GlideUtil.loadShapeableImageView(mContext, userAvatarUrl,
                    (ShapeableImageView) ((ViewHolderWithContent)holder).userAvatar,
                    mRequestOptions);

            ((ViewHolderWithContent)holder).username.setText(username);
            ((ViewHolderWithContent)holder).userUnit.setText(userHouseAddr);
            ((ViewHolderWithContent)holder).postTitle.setText(postTitle);
            ((ViewHolderWithContent)holder).postContent.setText(postContent);

            if(mPostItems.get(position).getPost().getPostTag().equals("无标签"))
                ((ViewHolderWithContent)holder).postTag.setVisibility(View.INVISIBLE);
            else
                ((ViewHolderWithContent)holder).postTag.setText(postTag);

            ((ViewHolderWithContent)holder).postDate.setText(postDate);
        }

        else{
            ImageView imageView = ((ViewHolderWithImg)holder).postImage;
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            long imageWidth = mPostItems.get(position).getImageItems().get(0).getWidth();
            long imageHeight = mPostItems.get(position).getImageItems().get(0).getHeight();
            int[] imageLayoutSize = ImageUtil.getLayoutSizeByWidth(mContext.getApplicationContext(), imageWidth, imageHeight);
            params.width = imageLayoutSize[0];
            params.height = imageLayoutSize[1];
            imageView.setLayoutParams(params);

            GlideUtil.loadShapeableImageView(mContext, userAvatarUrl,
                    (ShapeableImageView) ((ViewHolderWithImg)holder).userAvatar,
                    mRequestOptions);
            ((ViewHolderWithImg)holder).username.setText(username);
            ((ViewHolderWithImg)holder).userUnit.setText(userHouseAddr);
            ((ViewHolderWithImg)holder).postTitle.setText(postTitle);
            GlideUtil.loadImageView(mContext, mPostItems.get(position).getImageItems().get(0).getUrl(),
                    (ImageView) ((ViewHolderWithImg)holder).postImage, mRequestOptions);

            if(mPostItems.get(position).getPost().getPostTag().equals("无标签"))
                ((ViewHolderWithImg)holder).postTag.setVisibility(View.INVISIBLE);

            else
                ((ViewHolderWithImg)holder).postTag.setText(postTag);

            ((ViewHolderWithImg)holder).postDate.setText(postDate);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("wjj", "clicked:" + mPostItems.get(holder.getAdapterPosition()).getPost().getPostTitle());
                String postId = mPostItems.get(holder.getAdapterPosition()).getPost().getPostId();
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra("postId", postId);
                mContext.startActivity(intent);
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
