package com.grad.information.vote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {
    private String mText;
    private String mDisplayText;
    private int optionPos = -1;
    private Paint mPaint;
    public CustomProgressBar (Context context) {
        super(context);
        initPaint();
    }
    public CustomProgressBar (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }
    public CustomProgressBar (Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }
    @Override
    public synchronized void setProgress(int progress) {
        mDisplayText = mText + "    " + progress;
        super.setProgress(progress);
    }
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.setTextSize(50);
        this.mPaint.getTextBounds(mDisplayText, 0, mDisplayText.length(), rect);

        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(mDisplayText, 0, y, this.mPaint);
    }
    //初始化，画笔
    private void initPaint(){
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
    }

    public String getmText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public int getOptionPos() {
        return optionPos;
    }

    public void setOptionPos(int pos) {
        this.optionPos = pos;
    }
}
