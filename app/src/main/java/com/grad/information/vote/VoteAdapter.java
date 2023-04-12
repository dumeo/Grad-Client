package com.grad.information.vote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.grad.R;
import com.grad.constants.DefaultVals;
import com.grad.constants.VoteConstants;
import com.grad.databinding.ItemVoteBinding;
import com.grad.pojo.vote.VoteItem;
import com.grad.util.GlideUtil;

import java.util.List;

public class VoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<VoteItem> voteItemList;
    private ItemVoteBinding itemVoteBinding;

    public VoteAdapter(Context context, List<VoteItem> voteItemList) {
        this.context = context;
        this.voteItemList = voteItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemVoteBinding = ItemVoteBinding.inflate(LayoutInflater.from(context), parent, false);
        return new VoteViewHolder(itemVoteBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VoteItem voteItem = voteItemList.get(position);
        GlideUtil.loadShapeableImageView(context.getApplicationContext(),
                voteItem.getUser().getAvatarUrl(), (ShapeableImageView) ((VoteViewHolder)holder).userAvatar,
                GlideUtil.DefaultRequestOptions);
        ((VoteViewHolder)holder).username.setText(voteItem.getUser().getUsername());
        ((VoteViewHolder)holder).userAddr.setText(voteItem.getUser().getHouseAddr());
        ((VoteViewHolder)holder).voteTitle.setText(voteItem.getVote().getVoteTitle());
        ((VoteViewHolder)holder).voteContent.setText(voteItem.getVote().getVoteContent());
        ((VoteViewHolder)holder).voteCnt.setText(voteItem.getVote().getVoteCnt() + "");
        ((VoteViewHolder)holder).voteStatus.setText(voteItem.getVote().getVoteStatus());
        if(voteItem.getVote().getVoteStatus().equals(VoteConstants.VOTE_STATUS_VOTING)){
            ((VoteViewHolder)holder).voteStatus.setBackgroundResource(R.drawable.tv_background1);
        }
        else{
            ((VoteViewHolder)holder).voteStatus.setBackgroundResource(R.drawable.tv_background2);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                VoteItem voteItem_ = voteItemList.get(pos);
                Intent intent = new Intent(context.getApplicationContext(), ToVoteActivity.class);
                intent.putExtra("voteId", voteItem_.getVote().getVoteId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return voteItemList.size();
    }

    class VoteViewHolder extends RecyclerView.ViewHolder{
        public ImageView userAvatar;
        public TextView username;
        public TextView userAddr;
        public TextView voteTitle;
        public TextView voteContent;
        public TextView voteCnt;
        public TextView voteStatus;


        public VoteViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemVoteBinding.userAvatar;
            username = itemVoteBinding.userName;
            userAddr = itemVoteBinding.userAddr;
            voteTitle = itemVoteBinding.voteTitle;
            voteContent = itemVoteBinding.voteContent;
            voteCnt = itemVoteBinding.voteCount;
            voteStatus = itemVoteBinding.tvStatus;
        }
    }

    public List<VoteItem> getVoteItemList() {
        return voteItemList;
    }
}
