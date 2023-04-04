package com.grad.pojo;

import java.util.ArrayList;
import java.util.List;

public class CommentItem {
    private Comment comment;
    List<CommentItem> childComments = new ArrayList<>();
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

    public List<CommentItem> getChildComments() {
        return childComments;
    }
}
