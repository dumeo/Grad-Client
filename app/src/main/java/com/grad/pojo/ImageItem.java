package com.grad.pojo;

public class ImageItem {

    private long imgId;
    private String postId;
    private long imgOrder;
    private String url;
    private long width;
    private long height;

    public ImageItem() {
    }

    public ImageItem(long imgId, String postId, long imgOrder, String url) {
        this.imgId = imgId;
        this.postId = postId;
        this.imgOrder = imgOrder;
        this.url = url;
    }

    public ImageItem(long imgId, String postId, long imgOrder, String url, long width, long height) {
        this.imgId = imgId;
        this.postId = postId;
        this.imgOrder = imgOrder;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public long getImgId() {
        return imgId;
    }

    public void setImgId(long imgId) {
        this.imgId = imgId;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public long getImgOrder() {
        return imgOrder;
    }

    public void setImgOrder(long imgOrder) {
        this.imgOrder = imgOrder;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }
}

