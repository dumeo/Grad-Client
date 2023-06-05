package com.grad.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.grad.R;
import com.grad.databinding.LayoutInfoDialogBinding;
import com.grad.databinding.LayoutInputLinkBinding;

import cn.hutool.core.util.StrUtil;
import jp.wasabeef.richeditor.RichEditor;

public class DialogUtil {

    public static void showInputYoutubeDialog(Context context, RichEditor editor, int type) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入链接");

        // 创建一个EditText对象并添加到对话框中
        final EditText input = new EditText(context);
        input.setBackground(context.getResources().getDrawable(R.drawable.et_bg2));
        builder.setView(input);

        // 设置对话框的“确定”按钮点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = input.getText().toString();
                url = StrUtil.replace(url, "watch?v=", "embed/");
                editor.insertYoutubeVideo(url);
            }
        });

        // 设置对话框的“取消”按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击了“取消”按钮，不做任何处理
            }
        });

        // 显示对话框
        builder.show();
    }

    public static void showInputLinkDialog(Context context, RichEditor editor) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("输入链接");

        // 防止内存泄漏
        final com.grad.databinding.LayoutInputLinkBinding[] binding = {LayoutInputLinkBinding.inflate(LayoutInflater.from(context), null, false)};
        builder.setView(binding[0].getRoot());

        // 设置对话框的“确定”按钮点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = binding[0].etTitle.getText().toString();
                String link = binding[0].etLink.getText().toString();
                editor.insertLink(link, title);
                // 防止内存泄漏
                binding[0] = null;
            }
        });

        // 设置对话框的“取消”按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 防止内存泄漏
                binding[0] = null;
            }
        });

        // 显示对话框
        builder.show();
    }

    public static void showInfoDialog(Context context, String msg) {
        // 创建一个AlertDialog.Builder对象
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("注意");

        // 防止内存泄漏
        final com.grad.databinding.LayoutInfoDialogBinding[] binding = {LayoutInfoDialogBinding.inflate(LayoutInflater.from(context), null, false)};
        builder.setView(binding[0].getRoot());
        binding[0].tvMsg.setText(msg);

        // 设置对话框的“确定”按钮点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding[0] = null;
            }
        });

        // 设置对话框的“取消”按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 防止内存泄漏
                binding[0] = null;
            }
        });

        // 显示对话框
        builder.show();
    }


}
