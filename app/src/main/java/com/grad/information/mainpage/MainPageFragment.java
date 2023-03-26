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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grad.databinding.FragmentMainPageBinding;
import com.grad.pojo.PostItem;

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
        initView();
        fetchData();
        setUpRefreshListener();
        return view;
    }

    @Override
    public void onResume() {//??????????????????????????????????????????????
        super.onResume();
        if(mIsFirstOpened) mIsFirstOpened = false;
        else {
            binding.swipeRefresh.setRefreshing(true);
            DataFetcher.reFetchData(mHandler, mPostItems);
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
                        ItemAdapter adapter = (ItemAdapter) binding.recyclerviewMainPage.getAdapter();
                        assert adapter != null;
                        adapter.notifyDataSetChanged();
                        binding.swipeRefresh.setRefreshing(false);
                    }

                }
                return false;
            }
        });



    }

    private void fetchData(){
        if(mPostItems == null) mPostItems = new ArrayList<>();
        DataFetcher.fetcheData(mHandler, mPostItems);
    }






    private void setUpRefreshListener(){
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //load new data
                DataFetcher.reFetchData(mHandler, mPostItems);
                binding.swipeRefresh.setRefreshing(false);
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
//                    binding.progressbarLoadMore.setVisibility(View.VISIBLE);

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