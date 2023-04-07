package com.grad.pojo;

import java.util.List;


public class PostItem {


    private Post post;
    private PostUserInfo postUserInfo;
    private List<ImageItem> imageItems;
    private ClientToThisInfo clientToThisInfo;
    private PostInfo postInfo;

    public PostItem(Post post, PostUserInfo postUserInfo, List<ImageItem> imageItems) {
        this.post = post;
        this.postUserInfo = postUserInfo;
        this.imageItems = imageItems;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public PostUserInfo getPostUserInfo() {
        return postUserInfo;
    }

    public void setPostUserInfo(PostUserInfo postUserInfo) {
        this.postUserInfo = postUserInfo;
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    public ClientToThisInfo getClientToThisInfo() {
        return clientToThisInfo;
    }

    public void setClientToThisInfo(ClientToThisInfo clientToThisInfo) {
        this.clientToThisInfo = clientToThisInfo;
    }

    public PostInfo getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }
}