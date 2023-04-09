package com.grad.information.vote;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

public class OptionDeleteIV extends ShapeableImageView {
    int pos;
    public OptionDeleteIV(Context context) {
        super(context);
    }

    public OptionDeleteIV(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionDeleteIV(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
