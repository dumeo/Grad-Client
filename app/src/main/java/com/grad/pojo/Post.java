package com.grad.pojo;

public class Post {
    private long postId;
    private long uid;
    private long postType;
    private String postTitle;
    private String postContent;
    private String postTag;
    private long viewTimes;
    private String postDate;

    public Post(long postId, long uid, long postType, String postTitle, String postContent, String postTag, long viewTimes, String postDate) {
        this.postId = postId;
        this.uid = uid;
        this.postType = postType;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTag = postTag;
        this.viewTimes = viewTimes;
        this.postDate = postDate;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
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
