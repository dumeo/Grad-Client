package com.grad.information.mainpage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.constants.UserConstants;

import java.util.List;

public class NavigationGridViewAdapter extends BaseAdapter {
    private List<NavigationData> mNavigationDataList;
    private LayoutInflater mLayoutInflater;
    private int mPageIndex;
    private int mGridViewItemCnt = UserConstants.NAVIGATION_GRIDVIEW_ITEM_CNT;
    private Context mContext;

    public NavigationGridViewAdapter(Context context, List<NavigationData> mDatas, int mPageIndex) {
        this.mNavigationDataList = mDatas;
        this.mPageIndex = mPageIndex;
        mContext = context;
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
        TextView tv;
        ShapeableImageView iv;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.navigation_item, viewGroup, false);
            iv = view.findViewById(R.id.item_iv);
            tv = view.findViewById(R.id.item_tv);
        }else{
            iv = view.findViewById(R.id.item_iv);
            tv = view.findViewById(R.id.item_tv);
        }
        int pos = i + mPageIndex * mGridViewItemCnt;
        tv.setText(mNavigationDataList.get(pos).getTitle());
        iv.setImageResource(mNavigationDataList.get(pos).getAvatarUri());
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("wjj", "clicked:================" + i);
//            }
//        });
        return view;
    }
}

