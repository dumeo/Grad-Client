package com.grad.information.vote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.grad.constants.VoteConstants;
import com.grad.databinding.ActivityVoteListBinding;
import com.grad.information.infocategory.ItemSpaceDecoration;
import com.grad.pojo.vote.VoteItem;
import com.grad.service.VoteService;

import java.util.ArrayList;
import java.util.List;

public class VoteListActivity extends AppCompatActivity {
    private ActivityVoteListBinding mBinding;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    private boolean mIsFirstOpened = true;
    private WrapContentLinearLayoutManager mLayoutManager;
    private int mCurrentCount = 0;
    private VoteAdapter mVoteAdapter;
    private List<VoteItem> mVoteItems = new ArrayList<>();
    List<VoteItem> mDeltaVoteItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityVoteListBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        initHandler();
        initData();
        initView();
        initListener();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mIsFirstOpened) mIsFirstOpened = false;
        else {
            mBinding.swipeRefresh.setRefreshing(true);
            VoteService.fetchVotes(mHandler, mDeltaVoteItems, VoteConstants.TYPE_REFETCH);
        }
    }

    private void initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case VoteConstants.FETCH_VOTES_OK:{
                        mVoteItems.addAll(mDeltaVoteItems);
                        mLayoutManager = new WrapContentLinearLayoutManager(VoteListActivity.this, LinearLayoutManager.VERTICAL, false);
                        mBinding.rvVotes.setLayoutManager(mLayoutManager);
                        mVoteAdapter = new VoteAdapter(VoteListActivity.this, mVoteItems);
                        mBinding.rvVotes.setPadding(5, 5, 5, 5);
                        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
                        mBinding.rvVotes.addItemDecoration(decoration);
                        mBinding.rvVotes.setAdapter(mVoteAdapter);
                        mBinding.swipeRefresh.setEnabled(true);
                        break;
                    }
                    case VoteConstants.REFETCH_VOTES_OK:{
                        if(mVoteAdapter == null) initData();
                        else{
                            mVoteItems.clear();
                            mVoteAdapter.notifyDataSetChanged();
                            mVoteItems.addAll(mDeltaVoteItems);
                            mVoteAdapter.notifyDataSetChanged();
                            mBinding.swipeRefresh.setRefreshing(false);
                        }
                        break;
                    }
                    case VoteConstants.LOAD_MORE_VOTES_OK:{
                        mIsLodaing = false;
                        mBinding.progressbarLoadMore.setVisibility(View.INVISIBLE);

                        mVoteItems.addAll(mDeltaVoteItems);
                        mVoteAdapter.notifyDataSetChanged();

                        break;
                    }

                }

                return false;
            }
        });

    };


    private void initData(){
        mBinding.swipeRefresh.setEnabled(false);
        VoteService.fetchVotes(mHandler, mDeltaVoteItems, VoteConstants.TYPE_INIT);
    };


    private void initView(){}

    private void initListener(){
        mBinding.addVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoteListActivity.this, AddVoteActivity.class));
            }
        });

        mBinding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refetch votes
                VoteService.fetchVotes(mHandler, mDeltaVoteItems, VoteConstants.TYPE_REFETCH);
            }
        });

        mBinding.rvVotes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
//                Log.e("wjj", "lastVisiblePosition:" + lastVisiblePosition);
                if(!mBinding.swipeRefresh.isRefreshing() && !mIsLodaing && mVoteAdapter.getItemCount() > 1 && lastVisiblePosition >= mVoteAdapter.getItemCount() - 1){
                    mIsLodaing = true;
                    mBinding.progressbarLoadMore.setVisibility(View.VISIBLE);
                    //load more data...
                    mCurrentCount = mVoteAdapter.getItemCount();
                    String createDate = mVoteAdapter.getVoteItemList().get(mCurrentCount - 1).getVote().getCreateDate();
                    VoteService.loadMoreVotes(mHandler, createDate, mVoteItems);
                }

            }
        });

    }



}