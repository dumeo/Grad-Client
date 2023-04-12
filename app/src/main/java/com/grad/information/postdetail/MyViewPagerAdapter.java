package com.grad.information.postdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.grad.constants.DefaultVals;
import com.grad.pojo.ImageItem;
import com.grad.pojo.PostItem;
import com.grad.util.GlideUtil;

import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {
    private PostItem mPostItem;
    private Context mContext;
    private List<ImageItem> imgs;

    public MyViewPagerAdapter(Context mContext, PostItem mPostItem) {
        this.mPostItem = mPostItem;
        this.mContext = mContext;
        imgs = mPostItem.getImageItems();
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageItem imageItem = imgs.get(position);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(params);
            GlideUtil.loadImageView(mContext, imageItem.getUrl(), imageView, GlideUtil.DefaultRequestOptions);
            container.addView(imageView);
            return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
