package com.grad.information.addpost;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {
    private float startX, startY;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:{
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                startX = ev.getX();
                startY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float endX = ev.getX();
                float endY = ev.getY();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                if(distanceX > distanceY){
                    getParent().getParent().requestDisallowInterceptTouchEvent(true);
                }
                else{
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            }

            case MotionEvent.ACTION_UP:{
                getParent().getParent().requestDisallowInterceptTouchEvent(false);
                break;
            }

        }
        return super.dispatchTouchEvent(ev);
    }
}
