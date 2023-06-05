package com.grad.information.delpost;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grad.R;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.PostConstants;
import com.grad.databinding.ItemDeletePostBinding;
import com.grad.pojo.Post;

import java.util.List;

public class DeletePostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Handler handler;
    private List<Post> postList;
    private ItemDeletePostBinding mBinding;
    private Context mContext;

    public DeletePostAdapter(Handler handler, List<Post> postList, Context mContext) {
        this.handler = handler;
        this.postList = postList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ItemDeletePostBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = postList.get(position);
        ((MyViewHolder)holder).textView.setText(post.getPostTitle());
        ((MyViewHolder)holder).delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                Message message = Message.obtain();
                message.what = PostConstants.DELETE_THIS_POST;
                message.obj = pos;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public Button delButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = mBinding.deletePostTitle;
            delButton = mBinding.btDel;
        }
    }
}
