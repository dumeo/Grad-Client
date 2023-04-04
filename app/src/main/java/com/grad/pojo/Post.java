package com.grad.pojo;

public class Post {
    private String postId;
    private String uid;
    private long postType;
    private String postTitle;
    private String postContent;
    private String postTag;
    private long viewTimes;
    private String postDate;
    private long likeCnt;

    public Post(String postId, String uid, long postType, String postTitle, String postContent, String postTag, long viewTimes, String postDate) {
        this.postId = postId;
        this.uid = uid;
        this.postType = postType;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTag = postTag;
        this.viewTimes = viewTimes;
        this.postDate = postDate;
    }

    public Post(String postId, String uid, long postType, String postTitle, String postContent, String postTag, long viewTimes, String postDate, long likeCnt) {
        this.postId = postId;
        this.uid = uid;
        this.postType = postType;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTag = postTag;
        this.viewTimes = viewTimes;
        this.postDate = postDate;
        this.likeCnt = likeCnt;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public long getPostType() {
        return postType;
    }

    public void setPostType(long postType) {
        this.postType = postType;
    }


    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }


    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }


    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }


    public long getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(long viewTimes) {
        this.viewTimes = viewTimes;
    }


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public long getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(long likeCnt) {
        this.likeCnt = likeCnt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", uid=" + uid +
                ", postType=" + postType +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postTag='" + postTag + '\'' +
                ", viewTimes=" + viewTimes +
                ", postDate='" + postDate + '\'' +
                '}';
    }
}
