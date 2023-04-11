package com.grad.pojo.vote;


public class Vote {

    private String voteId;
    private String uid;
    private long voteType;
    private String voteTitle;
    private String voteContent;
    private String voteTag;
    private long viewTimes;
    private long voteCnt;
    private String voteStatus;
    private String createDate;
    private String endDate;

    public Vote(String voteId, String uid, long voteType, String voteTitle, String voteContent, String voteTag, long viewTimes, long voteCnt, String voteStatus, String createDate, String endDate) {
        this.voteId = voteId;
        this.uid = uid;
        this.voteType = voteType;
        this.voteTitle = voteTitle;
        this.voteContent = voteContent;
        this.voteTag = voteTag;
        this.viewTimes = viewTimes;
        this.voteCnt = voteCnt;
        this.voteStatus = voteStatus;
        this.createDate = createDate;
        this.endDate = endDate;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public long getVoteType() {
        return voteType;
    }

    public void setVoteType(long voteType) {
        this.voteType = voteType;
    }


    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }


    public String getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(String voteContent) {
        this.voteContent = voteContent;
    }


    public String getVoteTag() {
        return voteTag;
    }

    public void setVoteTag(String voteTag) {
        this.voteTag = voteTag;
    }


    public long getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(long viewTimes) {
        this.viewTimes = viewTimes;
    }


    public long getVoteCnt() {
        return voteCnt;
    }

    public void setVoteCnt(long voteCnt) {
        this.voteCnt = voteCnt;
    }


    public String getVoteStatus() {
        return voteStatus;
    }

    public void setVoteStatus(String voteStatus) {
        this.voteStatus = voteStatus;
    }


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}

