package com.grad.information.reserve;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.grad.App;
import com.grad.R;
import com.grad.constants.UserConstants;
import com.grad.databinding.FragmentToReserveBinding;
import com.grad.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.hutool.core.util.StrUtil;


public class ToReserveFragment extends Fragment {
    public String title;
    private FragmentToReserveBinding mBinding;
    private Context mContext;
    private Handler mHandler;
    private String mReserveDate;
    private  Calendar mCalendar;

    public ToReserveFragment(String title) {
        this.title = title;
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
        mBinding = FragmentToReserveBinding.inflate(inflater, container, false);
        initHandler();
        initData();
        initView();
        initListener();
        return mBinding.getRoot();
    }

    private void  initHandler(){
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case UserConstants.ADD_RESERVE_OK:{
                        Toast.makeText(mContext, "预约成功", Toast.LENGTH_SHORT).show();
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        break;
                    }
                    case UserConstants.ADD_RESERVE_FAILED:{
                        mBinding.progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "预约失败", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });
    }
    private void initData(){
        mCalendar = Calendar.getInstance();
        mBinding.datePicker.setMinDate(mCalendar.getTimeInMillis()+ 1*24*3600*1000);
        mBinding.datePicker.setMaxDate(mCalendar.getTimeInMillis() + 5*24*3600*1000);
        //默认预约日期为当前日期的下一天09:00分
        mReserveDate = mCalendar.get(Calendar.YEAR)
                + "-"
                + ((mCalendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (mCalendar.get(Calendar.MONTH) + 1) : (mCalendar.get(Calendar.MONTH) + 1))
                + "-"
                + mCalendar.get(Calendar.DAY_OF_MONTH)
                + " 09:00:00";
    }

    private void initView(){

    }

    private void initListener(){
        mBinding.ivCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.datePicker.setVisibility(View.VISIBLE);
                mBinding.btCalenderOk.setVisibility(View.VISIBLE);
            }
        });

        mBinding.datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(year, monthOfYear, dayOfMonth);
                mReserveDate = year
                        + "-"
                        + ((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : monthOfYear + 1)
                        + "-"
                        + dayOfMonth
                        + " 09:00:00";
                mBinding.tvSelectedDate.setText(mReserveDate.substring(0, 16));
            }
        });


        mBinding.btCalenderOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.datePicker.setVisibility(View.INVISIBLE);
                mBinding.btCalenderOk.setVisibility(View.INVISIBLE);
            }
        });

        mBinding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reserveContent = mBinding.etReserveContent.getText().toString();
                String phone = mBinding.etPhone.getText().toString();
                if(StrUtil.isEmpty(reserveContent)|| StrUtil.isEmpty(phone)){
                    Toast.makeText(mContext, "请将信息补充完整", Toast.LENGTH_SHORT).show();
                    return;
                }
                mBinding.progressBar.setVisibility(View.VISIBLE);
                mBinding.etReserveContent.setText("");
                mBinding.etPhone.setText("");
                ReserveItem reserveItem = new ReserveItem(null, App.getUser().getUid(),
                        null, reserveContent, phone, mReserveDate);
                UserService.addReserve(mHandler, reserveItem);

            }
        });
    }
}