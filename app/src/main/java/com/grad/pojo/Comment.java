package com.grad.pojo;


public class Comment {

    private String commentId;
    private String uid;
    private String postId;
    private String content;
    private long likeCnt;
    private String fatherId;
    private long commentLevel;
    private String commentDate;

    public Comment(String commentId, String uid, String postId, String content, long likeCnt, String fatherId, long commentLevel, String commentDate) {
        this.commentId = commentId;
        this.uid = uid;
        this.postId = postId;
        this.content = content;
        this.likeCnt = likeCnt;
        this.fatherId = fatherId;
        this.commentLevel = commentLevel;
        this.commentDate = commentDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public long getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(long likeCnt) {
        this.likeCnt = likeCnt;
    }


    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }


    public long getCommentLevel() {
        return commentLevel;
    }

    public void setCommentLevel(long commentLevel) {
        this.commentLevel = commentLevel;
    }


    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public String toString() {
        return "CommentTable{" +
                "commentId='" + commentId + '\'' +
                ", uid='" + uid + '\'' +
                ", postId='" + postId + '\'' +
                ", content='" + content + '\'' +
                ", likeCnt=" + likeCnt +
                ", fatherId='" + fatherId + '\'' +
                ", commentLevel=" + commentLevel +
                ", commentDate='" + commentDate + '\'' +
                '}';
    }
}
