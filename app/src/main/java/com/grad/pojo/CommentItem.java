package com.grad.pojo;

public class CommentItem {
    private Comment comment;
    private User user;

    public CommentItem(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public Comment getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }
}
