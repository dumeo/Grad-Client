package com.grad.information.infocategory;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class CustomStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public CustomStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try{
            super.onLayoutChildren(recycler, state);
        }catch (IndexOutOfBoundsException e){
            Log.e("wjj", "Meet IOOB Exception");
        }

    }
}
