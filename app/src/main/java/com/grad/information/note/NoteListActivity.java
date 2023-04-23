package com.grad.information.note;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.grad.constants.UserConstants;
import com.grad.databinding.ActivityNoteListBinding;
import com.grad.information.mainpage.ItemSpaceDecoration;
import com.grad.information.vote.WrapContentLinearLayoutManager;
import com.grad.pojo.User;
import com.grad.service.CommitteeService;
import com.grad.service.UserService;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private ActivityNoteListBinding mBinding;
    private Handler mHandler;
    private List<NoteItem> mDeltaList = new ArrayList<>();
    private List<NoteItem> mNoteItems = new ArrayList<>();
    private NoteItemAdapter mNoteItemAdapter;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initData();
        initListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UserConstants.GET_NOTES_OK:{
                        initView();
                        break;
                    }
                    case UserConstants.GET_NOTES_FAILED:{
                        Toast.makeText(NoteListActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case UserConstants.SHOW_THIS_NOTE:{
                        NoteItem noteItem = (NoteItem) msg.obj;
                        mBinding.rlDetail.setVisibility(View.VISIBLE);
                        mBinding.tvDetail.setText(noteItem.getContent());
                        UserService.readNote(noteItem.getNoteId());
                        break;
                    }

                }

                return false;
            }
        });
    }

    private void initData(){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE);
        String userStr = sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, null);
        mUser = JsonUtil.jsonToObject(userStr, User.class);
        UserService.getNotes(mHandler, mUser.getCommunityName(), mNoteItems);
    }
    private void initView(){
        mNoteItemAdapter = new NoteItemAdapter(mHandler, NoteListActivity.this, mNoteItems);
        WrapContentLinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(NoteListActivity.this);
        mBinding.rvNotes.setLayoutManager(layoutManager);
        mBinding.rvNotes.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mBinding.rvNotes.addItemDecoration(decoration);
        mBinding.rvNotes.setAdapter(mNoteItemAdapter);
    }

    private void initListener(){
        mBinding.btHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.rlDetail.setVisibility(View.INVISIBLE);
            }
        });
    }
}