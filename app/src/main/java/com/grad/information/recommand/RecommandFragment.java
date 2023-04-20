package com.grad.information.recommand;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.grad.R;
import com.grad.constants.RecommConstants;
import com.grad.constants.UserConstants;
import com.grad.databinding.FragmentRecommandBinding;
import com.grad.information.mainpage.CustomStaggeredGridLayoutManager;
import com.grad.information.mainpage.ItemAdapter;
import com.grad.information.mainpage.ItemSpaceDecoration;
import com.grad.pojo.PostItem;
import com.grad.pojo.User;
import com.grad.service.PostService;
import com.grad.service.RecommendService;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class RecommandFragment extends Fragment {
    private FragmentRecommandBinding mBinding;
    private Context mContext;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    private ItemAdapter mItemAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private List<PostItem> mPostItems = new ArrayList<>();
    private int mCurrentCount = 0;
    private List<PostItem> mDeltaPostItems = new ArrayList<>();
    private User mUser;

    public RecommandFragment() {}
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
        mBinding = FragmentRecommandBinding.inflate(inflater, container, false);
        mBinding.swipeRefresh.setEnabled(false);
        initHandler();
        initData();
        initListener();
        return mBinding.getRoot();
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case RecommConstants.FETCH_OK:{
                        initRecyclerView();
                        break;
                    }

                    case RecommConstants.REFETCH_OK:{
                        if(mItemAdapter == null){
                            RecommendService.getRecommend(mUser.getUid(), mHandler, mDeltaPostItems, RecommConstants.FETCH);
                            break;
                        }
                        if(mDeltaPostItems.size() != 0){
                            mPostItems.clear();
                            mItemAdapter.notifyDataSetChanged();
                            mPostItems.addAll(mDeltaPostItems);
                            mItemAdapter.notifyDataSetChanged();
                        }
                        mBinding.swipeRefresh.setRefreshing(false);
                        break;
                    }

                    case RecommConstants.LOAD_MORE_OK:{
                        mIsLodaing = false;
                        mBinding.progressLoadMore.setVisibility(View.INVISIBLE);
                        int start = mPostItems.size();
                        mPostItems.addAll(mDeltaPostItems);
                        mItemAdapter.notifyItemRangeChanged(start, mDeltaPostItems.size());
                        break;
                    }

                    case RecommConstants.FETCH_FAILED:{
                        if(mBinding.swipeRefresh.isRefreshing())
                            mBinding.swipeRefresh.setRefreshing(false);
                        if(mBinding.progressLoadMore.getVisibility() == View.VISIBLE){
                            mIsLodaing = false;
                            mBinding.progressLoadMore.setVisibility(View.INVISIBLE);
                        }
                        mBinding.swipeRefresh.setEnabled(true);
                        Toast.makeText(mContext, "发生错误", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                return false;
            }
        });
    }


    private void initData(){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(mContext, UserConstants.USER_INFO_DATABASE);
        String userStr = sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, null);
        mUser  = JsonUtil.jsonToObject(userStr, User.class);
        RecommendService.getRecommend(mUser.getUid(), mHandler, mDeltaPostItems, RecommConstants.FETCH);
    }
    private void initRecyclerView(){
        mPostItems.addAll(mDeltaPostItems);
        mItemAdapter = new ItemAdapter(mContext, mPostItems);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.invalidateSpanAssignments();
        mBinding.rvPosts.setLayoutManager(mLayoutManager);
        mBinding.rvPosts.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mBinding.rvPosts.addItemDecoration(decoration);
        mBinding.rvPosts.setAdapter(mItemAdapter);
        mBinding.swipeRefresh.setEnabled(true);
    }
    private void initListener(){

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecommendService.getRecommend(mUser.getUid(), mHandler, mDeltaPostItems, RecommConstants.REFETCH);
            }
        });


        mBinding.rvPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int[] lastVisiblePositions = mLayoutManager.findLastVisibleItemPositions(null);
                int lastVisiblePosition = getLastVisiblePosition(lastVisiblePositions);
                if(!mBinding.swipeRefresh.isRefreshing() && !mIsLodaing && lastVisiblePosition >= mItemAdapter.getItemCount() - 1){
                    mIsLodaing = true;
                    mBinding.progressLoadMore.setVisibility(View.VISIBLE);
                    //load more data...
                    RecommendService.getRecommend(mUser.getUid(), mHandler, mDeltaPostItems, RecommConstants.LOAD_MORE);
                }

            }
        });

    }

    private int getLastVisiblePosition(int[] lastVisiblePositions) {
        int lastVisiblePosition = lastVisiblePositions[0];
        for (int position : lastVisiblePositions) {
            if (position > lastVisiblePosition) {
                lastVisiblePosition = position;
            }
        }
        return lastVisiblePosition;
    }
}