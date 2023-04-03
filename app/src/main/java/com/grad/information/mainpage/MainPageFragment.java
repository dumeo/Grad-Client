package com.grad.information.mainpage;

import static com.grad.util.DefaultVals.FETCH_DATA_COMPLETED;
import static com.grad.util.DefaultVals.REFETCH_DATA_COMPLETED;

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

import com.grad.databinding.FragmentMainPageBinding;
import com.grad.http.DataFetcher;
import com.grad.pojo.PostItem;
import com.grad.service.PostService;
import com.grad.util.DefaultVals;

import java.util.ArrayList;
import java.util.List;


public class MainPageFragment extends Fragment {

    private Context mContext;
    private FragmentMainPageBinding binding;
    private StaggeredGridLayoutManager mLayoutManager;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    private ItemAdapter mItemAdapter;
    private List<PostItem> mPostItems;
    private boolean mIsFirstOpened = true;
    private int mCurrentCount = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public MainPageFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.swipeRefresh.setEnabled(false);
        Log.e("wjj", "main page fragment onCreateView");
        initView();
        fetchData();
        setUpRefreshListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mIsFirstOpened) mIsFirstOpened = false;
        else {
            PostService.reFetchData(mHandler, mPostItems);
            binding.swipeRefresh.setRefreshing(true);
        }
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case FETCH_DATA_COMPLETED:  {
                        mItemAdapter = new ItemAdapter(mContext, mPostItems);
                        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        mLayoutManager.invalidateSpanAssignments();
                        binding.recyclerviewMainPage.setLayoutManager(mLayoutManager);
                        binding.recyclerviewMainPage.setPadding(5, 5, 5, 5);
                        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
                        binding.recyclerviewMainPage.addItemDecoration(decoration);
                        binding.recyclerviewMainPage.setAdapter(mItemAdapter);
                        binding.swipeRefresh.setEnabled(true);
                    }

                    case REFETCH_DATA_COMPLETED:{
                        mItemAdapter.notifyDataSetChanged();
                        binding.recyclerviewMainPage.requestLayout();
                        binding.swipeRefresh.setRefreshing(false);
                    }
                    case DefaultVals.FETCH_DATA_FAILED:{
                        binding.swipeRefresh.setEnabled(true);
                        binding.swipeRefresh.setRefreshing(false);
                        break;
                    }

                    case DefaultVals.LOAD_MORE_DATA_COMPLETED:{
                        mIsLodaing = false;
                        binding.progressbarLoadMore.setVisibility(View.INVISIBLE);
                        mItemAdapter.notifyItemRangeChanged(mCurrentCount, mItemAdapter.getItemCount() - 1);
                        break;
                    }

                }
                return false;
            }
        });

    }


    private void fetchData(){
        if(mPostItems == null) mPostItems = new ArrayList<>();
        PostService.fetcheData(mHandler, mPostItems);
    }






    private void setUpRefreshListener(){
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //load new data
                PostService.reFetchData(mHandler, mPostItems);
            }
        });

        binding.recyclerviewMainPage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int[] lastVisiblePositions = mLayoutManager.findLastVisibleItemPositions(null);
                int lastVisiblePosition = getLastVisiblePosition(lastVisiblePositions);
//                Log.e("wjj", "lastVisiblePosition:" + lastVisiblePosition);
                if(!binding.swipeRefresh.isRefreshing() && !mIsLodaing && lastVisiblePosition >= mItemAdapter.getItemCount() - 1){
                    mIsLodaing = true;
                    binding.progressbarLoadMore.setVisibility(View.VISIBLE);
                    //load more data...
                    mCurrentCount = mItemAdapter.getItemCount();
                    Log.e("wjj", "current count = " + mCurrentCount);
                    String startTime = mItemAdapter.getmPostItems().get(mCurrentCount - 1).getPost().getPostDate();
                    Log.e("wjj", "startTime = " + startTime);
                    PostService.loadMorePosts(mHandler, startTime, mPostItems);
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