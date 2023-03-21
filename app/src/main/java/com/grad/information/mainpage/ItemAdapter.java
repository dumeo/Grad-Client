package com.grad.information.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grad.R;
import com.grad.databinding.ItemTypeImageBinding;
import com.grad.databinding.ItemTypeTextBinding;
import com.grad.pojo.PostItem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostItem> mPosts;
    private Context mContext;
    private ItemTypeImageBinding mItemTypeImageBinding;
    private ItemTypeTextBinding mItemTypeTextBinding;

    public ItemAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        // 返回 View 布局类型, 奇数序号组件类型为 VIEW_TYPE_2, 偶数序号组件类型为 VIEW_TYPE_1
        return position % 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        mItemTypeImageBinding = ItemTypeImageBinding.inflate(LayoutInflater.from(mContext), parent, false);
//        mItemTypeTextBinding = ItemTypeTextBinding.inflate(LayoutInflater.from(mContext), parent, false);
//

        if(viewType == 0){
            return new ViewHolderWithImg( LayoutInflater.from(mContext).inflate(R.layout.item_type_image, parent, false));
        }

        return new ViewHolderWithContent( LayoutInflater.from(mContext).inflate(R.layout.item_type_text, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 0)
            ((ViewHolderWithImg)holder).tag.setText("这是图片贴");

        else ( (ViewHolderWithContent)holder).tag.setText("这是文字贴");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolderWithImg extends RecyclerView.ViewHolder{
        TextView tag;
        public ViewHolderWithImg(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.post_tag);
        }
    }

    class ViewHolderWithContent extends RecyclerView.ViewHolder{
        TextView tag;
        public ViewHolderWithContent(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.post_tag2);
        }
    }
}
