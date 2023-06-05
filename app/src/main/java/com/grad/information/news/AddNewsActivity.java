package com.grad.information.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.grad.App;
import com.grad.R;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.FileConstants;
import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityAddNewsBinding;
import com.grad.service.CommitteeService;
import com.grad.service.FileService;
import com.grad.util.DialogUtil;
import com.grad.util.GlideUtil;
import com.grad.util.UriUtil;

import cn.hutool.core.util.StrUtil;
import jp.wasabeef.richeditor.RichEditor;

public class AddNewsActivity extends AppCompatActivity {
    ActivityAddNewsBinding mBinding;
    private RichEditor mEditor;
    private final int IMG_REQUEST = 0;
    private final int VIDEO_REQUEST = 1;
    private final int AUDIO_REQUEST = 2;
    private final int HEAD_IMG_REQUEST = 3;
    private Handler mHandler;
    private String mHeadImgUrl;
    String[] mPermissionList = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAddNewsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initPermission();
        initHandler();
        initView();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            String filePath = UriUtil.getRealPathFromUri(getApplicationContext(), uri);
            if(requestCode == IMG_REQUEST){
                mBinding.progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileService.uploadFile(mHandler, filePath, FileConstants.IMG);
                    }
                }).start();
            }else if(requestCode == VIDEO_REQUEST){
                mBinding.progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileService.uploadFile(mHandler, filePath, FileConstants.VIDEO);
                    }
                }).start();
            }else if(requestCode == AUDIO_REQUEST){
                mBinding.progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileService.uploadFile(mHandler, filePath, FileConstants.AUDIO);
                    }
                }).start();
            }else if(requestCode == HEAD_IMG_REQUEST){
                mBinding.imgProgressBar.setVisibility(View.VISIBLE);
                //插入图片
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //将图片传入后端
                        CommitteeService.uploadHeadImg(mHandler, filePath);
                    }
                }).start();
            }
        }
        if(data == null) mEditor.setEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {

                } else {
                    Toast.makeText(this, "请设置必要权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case FileConstants.UPLOAD_IMG_OK:{
                        mEditor.setEnabled(true);
                        //后端返回的图片url
                        String imgUrl = (String) msg.obj;
                        //使用富文本编辑器插入图片url
                        mEditor.insertImage(imgUrl, "wjj", 300);
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }

                    case FileConstants.UPLOAD_FILE_FAILED:{
                        mEditor.setEnabled(true);
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }

                    case FileConstants.UPLOAD_VIDEO_OK:{
                        mEditor.setEnabled(true);
                        String videoUrl = (String) msg.obj;
                        mEditor.insertVideo(videoUrl, 300);
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case FileConstants.FILE_SIZE_EXCEEZED:{
                        mEditor.setEnabled(true);
                        Toast.makeText(AddNewsActivity.this, "文件大小超出20MB", Toast.LENGTH_SHORT).show();
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case FileConstants.UPLOAD_AUDIO_OK:{
                        mEditor.setEnabled(true);
                        String audioUrl = (String) msg.obj;
                        mEditor.insertAudio(audioUrl);
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case CommitteeConstants.UPLOAD_NEWS_OK:{
                        Toast.makeText(AddNewsActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    }
                    case CommitteeConstants.UPLOAD_NEWS_FAILED:{
                        Toast.makeText(AddNewsActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case CommitteeConstants.UPLOAD_HEAD_IMG_OK:{
                        String url = (String) msg.obj;
                        mHeadImgUrl = url;
                        GlideUtil.loadImageView(AddNewsActivity.this, url, mBinding.ivHead, GlideUtil.DefaultRequestOptions);
                        mBinding.imgProgressBar.setVisibility(View.INVISIBLE);
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void initPermission(){
        ActivityCompat.requestPermissions(AddNewsActivity.this, mPermissionList, 100);
    }

    private void initView(){
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(400);
        mEditor.setEditorFontSize(18);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("输入内容...");
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {

            }
        });

        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, IMG_REQUEST);
                mEditor.setEnabled(false);
            }
        });

        findViewById(R.id.action_insert_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, VIDEO_REQUEST);
                mEditor.setEnabled(false);
            }
        });

        findViewById(R.id.action_insert_youtube).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEditor.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA");
                DialogUtil.showInputYoutubeDialog(AddNewsActivity.this, mEditor, UserConstants.INPUT_TYPE_YOUTUBE_LINK);
            }
        });

        findViewById(R.id.action_insert_audio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, AUDIO_REQUEST);
                mEditor.setEnabled(false);
            }
        });


        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
                DialogUtil.showInputLinkDialog(AddNewsActivity.this, mEditor);
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });

        mBinding.btPickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(intent, HEAD_IMG_REQUEST);
            }
        });

        mBinding.btSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String communityName = App.getUser().getCommunityName();
                String content = mEditor.getHtml();
                String title = mBinding.etTitle.getText().toString();
                if(mHeadImgUrl == null){
                    Toast.makeText(AddNewsActivity.this, "请选择封面图片", Toast.LENGTH_SHORT).show();
                    return;
                }else if(StrUtil.isEmpty(title) || StrUtil.isEmpty(content)){
                    Toast.makeText(AddNewsActivity.this, "请将内容补充完整", Toast.LENGTH_SHORT).show();
                }
                CommitteeService.uploadNews(mHandler, new CommunityNews(null, communityName, title, content, mHeadImgUrl, 0, null));
            }
        });

    }
}