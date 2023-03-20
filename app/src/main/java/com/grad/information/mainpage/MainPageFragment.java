package com.grad.information.mainpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grad.R;


public class MainPageFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Context mContext;

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
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        initView(view);
        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }



    private void initView(View view){
        mRecyclerView = view.findViewById(R.id.recyclerview_main_page);
        AdapterMainPageReclv adapterMainPageReclv = new AdapterMainPageReclv(mContext);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setPadding(5, 5, 5, 5);
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(10);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(adapterMainPageReclv);
    }
}