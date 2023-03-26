package com.grad.information.addpost;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
