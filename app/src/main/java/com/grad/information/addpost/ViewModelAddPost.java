package com.grad.information.addpost;

import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.grad.util.DefaultVals;

import cn.hutool.core.util.StrUtil;

public class ViewModelAddPost {
    private MutableLiveData<String> mTitle;
    private MutableLiveData<String> mContent;
    private MutableLiveData<String> mTag;
    public final AdapterView.OnItemSelectedListener mOnItemSelectedListener;

    public ViewModelAddPost() {
        mTitle = new MutableLiveData<>();
        mContent = new MutableLiveData<>();
        mTag = new MutableLiveData<>();
        mOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tag = parent.getItemAtPosition(position).toString();
                mTag.postValue(tag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String tag = parent.getItemAtPosition(0).toString();
                mTag.postValue(tag);
            }
        };
    }

    public String getTitle() {
        return mTitle.getValue();
    }
    public String getTag() {
        return mTag.getValue();
    }

    public void setTitle(String title) {
        this.mTitle.setValue(title);
    }

    public String getContent() {
        return mContent.getValue();
    }

    public void setContent(String content) {
        this.mContent.setValue(content);
    }


}
