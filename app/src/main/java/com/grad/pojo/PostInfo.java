package com.grad.pojo;

import java.util.List;

public class PostInfo {
    private long commentCnt;
    List<ImageItem> imageItems;
    public PostInfo() {
    }

    public long getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(long commentCnt) {
        this.commentCnt = commentCnt;
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }
}
