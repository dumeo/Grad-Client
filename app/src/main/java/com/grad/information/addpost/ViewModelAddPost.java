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
    private MutableLiveData<Integer> mStatus;
    public final AdapterView.OnItemSelectedListener mOnItemSelectedListener;

    public ViewModelAddPost() {
        mTitle = new MutableLiveData<>();
        mContent = new MutableLiveData<>();
        mTag = new MutableLiveData<>();
        mStatus = new MutableLiveData<>(DefaultVals.ADD_POST_EDITING);
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
