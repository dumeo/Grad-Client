package com.grad.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.grad.R;

public class ImageUtil {

    //Get the new width and height by a given width(here is the screen width)
    public static int[] getLayoutSizeByWidth(Context context, long imageWidth, long imageHeight){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //The given width
        double layoutWidth = displayMetrics.widthPixels / displayMetrics.density * 1.42;
        double layoutHeight = layoutWidth * ((imageHeight/1.0) / (imageWidth/1.0));
        return new int[]{(int) Math.floor(layoutWidth), (int) Math.floor(layoutHeight)
        };
    }

    //get new layout size by a given height
    public static int[] getLayoutSizeByHeight(Context context, long imageWidth, long imageHeight){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float image_viewpager_height = context.getResources().getDimension(R.dimen.image_viewpager_height);
        //The given height
        double h = image_viewpager_height / displayMetrics.density;
        double w = (imageWidth/1.0) / (imageHeight/1.0) * h;
        return new int[]{(int)Math.floor(w), (int)Math.floor(h)};
    };

}
