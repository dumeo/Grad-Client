package com.grad.pojo;

import java.util.List;


public class PostItem {


    private Post post;
    private User user;
    private ClientToThisInfo clientToThisInfo;
    private PostInfo postInfo;

    public PostItem(Post post, User user, ClientToThisInfo clientToThisInfo, PostInfo postInfo) {
        this.post = post;
        this.user = user;
        this.clientToThisInfo = clientToThisInfo;
        this.postInfo = postInfo;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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