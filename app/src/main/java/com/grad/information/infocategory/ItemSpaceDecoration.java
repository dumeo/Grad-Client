package com.grad.information.infocategory;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemSpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ItemSpaceDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        outRect.top = space;
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
    }
}
