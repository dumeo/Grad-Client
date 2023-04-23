package com.grad.information.note;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grad.constants.UserConstants;
import com.grad.databinding.NoteItemBinding;

import java.util.List;

public class NoteItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Handler mHandler;
    private Context mContext;
    private NoteItemBinding mBinding;
    private List<NoteItem> mNoteItems;

    public NoteItemAdapter(Handler mHandler, Context mContext, List<NoteItem> mNoteItems) {
        this.mHandler = mHandler;
        this.mContext = mContext;
        this.mNoteItems = mNoteItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = NoteItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).content.setText(mNoteItems.get(position).getContent());
        ((MyViewHolder)holder).info.setText(mNoteItems.get(position).getCommunityName() +
                "  " + mNoteItems.get(position).getCreateDate());
        ((MyViewHolder)holder).readCnt.setText(mNoteItems.get(position).getReadCnt() + "次阅读");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Message message = Message.obtain();
                message.what = UserConstants.SHOW_THIS_NOTE;
                message.obj = mNoteItems.get(pos);
                mHandler.sendMessage(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoteItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView info;
        public TextView readCnt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            content = mBinding.tvContent;
            info = mBinding.tvInfo;
            readCnt = mBinding.tvReadCnt;
        }
    }

}
