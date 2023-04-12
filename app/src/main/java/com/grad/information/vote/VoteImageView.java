package com.grad.information.vote;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class VoteImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int optionPos = -1;

    public VoteImageView(Context context) {
        super(context);
    }

    public VoteImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VoteImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getOptionPos() {
        return optionPos;
    }

    public void setOptionPos(int optionPos) {
        this.optionPos = optionPos;
    }
}
