package com.grad.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

public class GlideUtil {

    public static void loadShapeableImageView(Context context,
                            String url,
                            ShapeableImageView imageView,
                            RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    public static void loadImageView(Context context,
                                              String url,
                                              ImageView imageView,
                                              RequestOptions options) {
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

}
