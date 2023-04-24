package com.grad.information.reserve;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.grad.App;
import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.databinding.FragmentReserveListBinding;
import com.grad.information.mainpage.ItemSpaceDecoration;
import com.grad.information.note.NoteListActivity;
import com.grad.information.vote.WrapContentLinearLayoutManager;
import com.grad.service.UserService;
import com.grad.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;


public class ReserveListFragment extends Fragment {
    public String title;
    private FragmentReserveListBinding mBinding;
    private Context mContext;
    private List<ReserveItem> mReserveItemList = new ArrayList<>();
    private List<ReserveItem> mDeltaList = new ArrayList<>();
    private Handler mHandler;
    private ReserveItemAdapter mItemAdapter;

    public ReserveListFragment(String title) {
        this.title = title;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mHandler != null) {
            UserService.getUserReserve(mHandler, App.getUser().getUid(), mDeltaList);
        }

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentReserveListBinding.inflate(inflater, container, false);
        initHandler();
        initData();
        initView();
        initListener();
        return mBinding.getRoot();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UserConstants.SHOW_RESERVE_QR:{
                        mBinding.rvReserve.setVisibility(View.INVISIBLE);
                        mBinding.rlQr.setVisibility(View.VISIBLE);
                        ReserveItem reserveItem = (ReserveItem) msg.obj;
                        GlideUtil.loadImageView(mContext, reserveItem.getQrUrl(),
                                mBinding.ivQr, GlideUtil.DefaultRequestOptions);
                        break;
                    }

                    case UserConstants.GET_RESERVE_OK:{
                        if(mItemAdapter == null){
                            initView();
                        }else{
                            mReserveItemList.clear();
                            mItemAdapter.notifyDataSetChanged();
                            mReserveItemList.addAll(mDeltaList);
                            mItemAdapter.notifyDataSetChanged();
                            mBinding.tvCnt.setText("您有" + mReserveItemList.size() + "项办事预约");
                        }
                        mBinding.swipeRefresh.setRefreshing(false);
                        break;
                    }

                    case UserConstants.GET_RESERVE_FAILED:{
                        mBinding.swipeRefresh.setRefreshing(false);
                        Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void initData(){
        UserService.getUserReserve(mHandler, App.getUser().getUid(), mDeltaList);
    }
    private void initView(){
        mReserveItemList.addAll(mDeltaList);
        mItemAdapter = new ReserveItemAdapter(mContext, mReserveItemList, mHandler);
        LinearLayoutManager layoutManager = new WrapContentLinearLayoutManager(mContext);
        mBinding.rvReserve.setLayoutManager(layoutManager);
        mBinding.rvReserve.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mBinding.rvReserve.addItemDecoration(decoration);
        mBinding.rvReserve.setAdapter(mItemAdapter);
        //-----
        mBinding.tvCnt.setText("您有" + mReserveItemList.size() + "项办事预约");
    }

    private void initListener(){
        mBinding.ivHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.rlQr.setVisibility(View.INVISIBLE);
                mBinding.rvReserve.setVisibility(View.VISIBLE);
            }
        });//

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserService.getUserReserve(mHandler, App.getUser().getUid(), mDeltaList);
            }
        });
    }


}