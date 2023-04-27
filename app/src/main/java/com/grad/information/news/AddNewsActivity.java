package com.grad.information.news;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.grad.R;
import com.grad.databinding.ActivityAddNewsBinding;

public class AddNewsActivity extends AppCompatActivity {
    ActivityAddNewsBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddNewsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initView();
    }

    private void initView(){
        mBinding.editor.setEditorHeight(200);
        mBinding.editor.setEditorFontSize(22);
        mBinding.editor.setEditorFontColor(Color.RED);
        mBinding.editor.setEditorBackgroundColor(Color.BLUE);
//        mBinding.editor.setBackgroundResource(R.mipmap.test_img);
        mBinding.editor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mBinding.editor.setPadding(10, 10, 10, 10);
        mBinding.editor.setPlaceholder("Insert text here...");





    }
}