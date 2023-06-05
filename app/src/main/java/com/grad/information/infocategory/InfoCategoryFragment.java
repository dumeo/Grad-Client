package com.grad.information.infocategory;

import static com.grad.constants.DefaultVals.FETCH_DATA_COMPLETED;
import static com.grad.constants.DefaultVals.REFETCH_DATA_COMPLETED;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
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

import com.grad.constants.PostConstants;
import com.grad.databinding.FragmentInfoCategoryBinding;
import com.grad.pojo.Post;
import com.grad.pojo.PostItem;
import com.grad.service.PostService;
import com.grad.constants.DefaultVals;

import java.util.ArrayList;
import java.util.List;


public class InfoCategoryFragment extends Fragment {

    private Context mContext;
    private FragmentInfoCategoryBinding binding;
    private CustomStaggeredGridLayoutManager mLayoutManager;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    private ItemAdapter mItemAdapter;
    private List<PostItem> mPostItems = new ArrayList<>();
    private int mCurrentCount = 0;
    private List<PostItem> mDeltaPostItems = new ArrayList<>();
    private String mPostTag;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public InfoCategoryFragment(String postTag) {mPostTag = postTag;}

    public void reloadPostsByTag(String postTag){
        mPostTag = postTag;
        mPostItems.clear();
        mItemAdapter.notifyDataSetChanged();
        mCurrentCount = 0;
        binding.swipeRefresh.setRefreshing(true);
        PostService.fetchData(mPostTag, mHandler, mDeltaPostItems, PostConstants.TYPE_REFETCH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoCategoryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.swipeRefresh.setEnabled(false);
        initView();
        fetchData(PostConstants.TYPE_INIT);
        setUpRefreshListener();
        return view;
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
                        Log.e("wjj", "Fetch data ok, size = " + mDeltaPostItems.size());
                        mPostItems.addAll(mDeltaPostItems);
                        mItemAdapter = new ItemAdapter(mContext, mPostItems);
                        mLayoutManager = new CustomStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        mLayoutManager.invalidateSpanAssignments();
                        binding.recyclerviewMainPage.setLayoutManager(mLayoutManager);
                        binding.recyclerviewMainPage.setPadding(5, 5, 5, 5);
                        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
                        binding.recyclerviewMainPage.addItemDecoration(decoration);
                        binding.recyclerviewMainPage.setAdapter(mItemAdapter);
                        binding.swipeRefresh.setEnabled(true);
                    }

                    case REFETCH_DATA_COMPLETED:{
                        if(mItemAdapter == null){
                            fetchData(PostConstants.TYPE_INIT);
                            break;
                        }
                        mPostItems.clear();
                        mItemAdapter.notifyDataSetChanged();
                        mPostItems.addAll(mDeltaPostItems);
                        mItemAdapter.notifyDataSetChanged();
                        binding.swipeRefresh.setRefreshing(false);
                        break;
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


    private void fetchData(int fetchType){
        binding.swipeRefresh.setRefreshing(true);
        PostService.fetchData(mPostTag, mHandler, mDeltaPostItems, fetchType);
    }

    private void setUpRefreshListener(){
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //load new data
                fetchData(PostConstants.TYPE_REFETCH);
            }
        });

        binding.recyclerviewMainPage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] lastVisiblePositions = mLayoutManager.findLastVisibleItemPositions(null);
                int lastVisiblePosition = getLastVisiblePosition(lastVisiblePositions);
                Log.e("wjj", "lastVisiblePosition:" + lastVisiblePosition);
                //判断用户是否已经浏览到底部
                if(mPostTag != null
                        && !binding.swipeRefresh.isRefreshing()
                        && !mIsLodaing
                        && lastVisiblePosition >= mItemAdapter.getItemCount() - 1){
                    mIsLodaing = true;
                    binding.progressbarLoadMore.setVisibility(View.VISIBLE);
                    mCurrentCount = mItemAdapter.getItemCount();
                    String startTime = mItemAdapter.getmPostItems().get(mCurrentCount - 1).getPost().getPostDate();
                    //若浏览到底部，自动请求更多信息
                    PostService.loadMorePosts(mPostTag, mHandler, startTime, mPostItems);
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