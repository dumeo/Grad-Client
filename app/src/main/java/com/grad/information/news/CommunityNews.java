package com.grad.information.news;

public class CommunityNews {
    private String newsId;
    private String communityName;
    private String newsTitle;
    private String newsContent;
    private String headImg;
    private long viewCnt;
    private String createDate;

    public CommunityNews(String newsId, String communityName, String newsTitle, String newsContent, String headImg, long viewCnt, String createDate) {
        this.newsId = newsId;
        this.communityName = communityName;
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.headImg = headImg;
        this.viewCnt = viewCnt;
        this.createDate = createDate;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }


    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }


    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }


    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }


    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }


    public long getViewCnt() {
        return viewCnt;
    }

    public void setViewCnt(long viewCnt) {
        this.viewCnt = viewCnt;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
