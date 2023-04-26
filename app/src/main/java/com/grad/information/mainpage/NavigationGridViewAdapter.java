package com.grad.information.mainpage;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.constants.PostConstants;
import com.grad.constants.UserConstants;
import com.grad.information.reserve.ReserveActivity;
import com.grad.information.vote.VoteListActivity;

import java.util.List;

public class NavigationGridViewAdapter extends BaseAdapter {
    private List<NavigationData> mNavigationDataList;
    private int mPageIndex;
    private int mGridViewItemCnt = UserConstants.NAVIGATION_GRIDVIEW_ITEM_CNT;
    private Context mContext;
    private Handler mHandler;

    public NavigationGridViewAdapter(Handler handler, Context context, List<NavigationData> mDatas, int mPageIndex) {
        this.mNavigationDataList = mDatas;
        this.mPageIndex = mPageIndex;
        mContext = context;
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return mNavigationDataList.size() > (mPageIndex + 1) * mGridViewItemCnt ? mGridViewItemCnt : (mNavigationDataList.size() - mPageIndex * mGridViewItemCnt);

    }

    @Override
    public Object getItem(int i) {
        return mNavigationDataList.get(i + mPageIndex * mGridViewItemCnt);
    }

    //数据集item的index
    @Override
    public long getItemId(int i) {
        return i + (long) mPageIndex * mGridViewItemCnt;
    }


    class ViewHolder {
        public TextView tv;
        public ShapeableImageView iv;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.navigation_item, viewGroup, false);
            holder = new ViewHolder();
            holder.iv = view.findViewById(R.id.item_iv);
            holder.tv = view.findViewById(R.id.item_tv);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        int pos = i + mPageIndex * mGridViewItemCnt;
        holder.tv.setText(mNavigationDataList.get(pos).getTitle());
        holder.iv.setImageResource(mNavigationDataList.get(pos).getAvatarUri());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = mNavigationDataList.get(pos).getTitle();
                if(tag.equals(mContext.getResources().getString(R.string.tag_bsyy))){
                    mContext.startActivity(new Intent(mContext, ReserveActivity.class));
                }else if(tag.equals(mContext.getResources().getString(R.string.tag_sqtp))){
                    mContext.startActivity(new Intent(mContext, VoteListActivity.class));
                }else{
                    Message message = Message.obtain();
                    message.what = PostConstants.SHOW_THESE_POST;
                    message.obj = tag;
                    mHandler.sendMessage(message);
                }
            }
        });
        return view;
    }
}

