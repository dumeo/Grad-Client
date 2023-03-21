package com.grad.information.mainpage;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grad.databinding.FragmentMainPageBinding;
import com.grad.pojo.PostItem;

import java.util.List;


public class MainPageFragment extends Fragment {

    private Context mContext;
    private FragmentMainPageBinding binding;
    private StaggeredGridLayoutManager mLayoutManager;
    private Handler mHandler;
    private boolean mIsLodaing = false;
    private ItemAdapter mItemAdapter;
    private List<PostItem> mPosts;



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
        mContext = view.getContext();
        initView();
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
        mHandler = new Handler();
        mItemAdapter = new ItemAdapter(mContext);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerviewMainPage.setLayoutManager(mLayoutManager);
        binding.recyclerviewMainPage.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        binding.recyclerviewMainPage.addItemDecoration(decoration);
        binding.recyclerviewMainPage.setAdapter(mItemAdapter);
    }

    private void setUpRefreshListener(){
        binding.swipeLoadMore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //load more data

                binding.swipeLoadMore.setRefreshing(false);
            }
        });

        binding.recyclerviewMainPage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int[] lastVisiblePositions = mLayoutManager.findLastVisibleItemPositions(null);
                int lastVisiblePosition = getLastVisiblePosition(lastVisiblePositions);

                if(!mIsLodaing && lastVisiblePosition >= mItemAdapter.getItemCount() - 1){
                    mIsLodaing = true;
                    binding.progressbarLoadMore.setVisibility(View.VISIBLE);

                    //load more data...
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