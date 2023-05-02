package com.grad.information.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grad.databinding.ItemNewsBinding;
import com.grad.information.news.CommunityNews;
import com.grad.information.news.NewsDetailActivity;
import com.grad.util.GlideUtil;

import org.w3c.dom.Text;

import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CommunityNews> communityNewsList;
    private ItemNewsBinding mBinding;

    public NewsItemAdapter(Context mContext, List<CommunityNews> communityNewsList) {
        this.mContext = mContext;
        this.communityNewsList = communityNewsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ItemNewsBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GlideUtil.loadImageView(mContext,
                communityNewsList.get(position).getHeadImg(),
                ((ViewHolder)holder).headImg,
                GlideUtil.DefaultRequestOptions);
        ((ViewHolder)holder).title.setText(communityNewsList.get(position).getNewsTitle());
        ((ViewHolder)holder).viewCnt.setText("" + communityNewsList.get(position).getViewCnt() + "次浏览");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                communityNewsList.get(pos).setViewCnt(communityNewsList.get(pos).getViewCnt() + 1);
                notifyItemChanged(pos);
                String html = communityNewsList.get(pos).getNewsContent();
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("html", html);
                intent.putExtra("news_id", communityNewsList.get(pos).getNewsId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return communityNewsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView headImg;
        public TextView title;
        public TextView viewCnt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headImg = mBinding.headImg;
            title = mBinding.title;
            viewCnt = mBinding.viewCnt;
        }
    }
}
