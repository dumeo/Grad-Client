package com.grad.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class DimensUtil {
    public static int getValues(Context context, int resId){
        float tmp = context.getResources().getDimension(resId);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        double h = tmp / displayMetrics.density;
        return (int) Math.floor(h);
    }
}
