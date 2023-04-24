package com.grad.information.reserve;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grad.constants.UserConstants;
import com.grad.databinding.ItemReserveBinding;

import java.util.ConcurrentModificationException;
import java.util.List;

public class ReserveItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ReserveItem> reserveItemList;
    private ItemReserveBinding mBinding;
    private Handler mHandler;

    public ReserveItemAdapter(Context mContext, List<ReserveItem> reserveItemList, Handler mHandler) {
        this.mContext = mContext;
        this.reserveItemList = reserveItemList;
        this.mHandler = mHandler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ItemReserveBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SpannableString spannableString = new SpannableString(mBinding.tvShowQr.getText());
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
        ((MyViewHolder)holder).showQrCode.setText(spannableString);
        ((MyViewHolder)holder).reserveContent.setText(reserveItemList.get(position).getReserveContent());
        ((MyViewHolder)holder).reserveDate.setText(reserveItemList.get(position).getReserveDate());

        ((MyViewHolder)holder).showQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Message message = Message.obtain();
                message.what = UserConstants.SHOW_RESERVE_QR;
                message.obj = reserveItemList.get(pos);
                mHandler.sendMessage(message);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reserveItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView reserveContent;
        public TextView reserveDate;
        public TextView showQrCode;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            reserveContent = mBinding.tvReserveContent;
            reserveDate = mBinding.tvDate;
            showQrCode = mBinding.tvShowQr;
        }
    }
}
