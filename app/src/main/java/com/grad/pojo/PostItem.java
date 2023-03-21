package com.grad.pojo;

import java.util.List;

import java.util.List;

public class PostItem {

    private long postId;
    private long postType;
    private long uid;
    private String avatarUrl; //
    private String username; //
    private String userHouseAddr; //
    private List<String> mediaUrl;
    private String postTitle; //
    private String postContent; //
    private String postTag; //
    private long viewTimes; //
    private String postDate; //

    public PostItem() {
    }

    public PostItem(long postId,  long uid, long postType, String avatarUrl, String username, String userHouseAddr, List<String> imgUrl, String postTitle, String postContent, String postTag, long viewTimes, String postDate) {
        this.postId = postId;
        this.postType = postType;
        this.uid = uid;
        this.avatarUrl = avatarUrl;
        this.username = username;
        this.userHouseAddr = userHouseAddr;
        this.mediaUrl = imgUrl;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTag = postTag;
        this.viewTimes = viewTimes;
        this.postDate = postDate;
    }

    public long getPostType() {
        return postType;
    }

    public void setPostType(long postType) {
        this.postType = postType;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserHouseAddr() {
        return userHouseAddr;
    }

    public void setUserHouseAddr(String userHouseAddr) {
        this.userHouseAddr = userHouseAddr;
    }

    public List<String> getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(List<String> mediaUrl) {
        this.mediaUrl = mediaUrl;
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
        return "PostItem{" +
                "postId=" + postId +
                ", postType=" + postType +
                ", uid=" + uid +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", username='" + username + '\'' +
                ", userHouseAddr='" + userHouseAddr + '\'' +
                ", mediaUrl=" + mediaUrl +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postTag='" + postTag + '\'' +
                ", viewTimes=" + viewTimes +
                ", postDate='" + postDate + '\'' +
                '}';
    }
}