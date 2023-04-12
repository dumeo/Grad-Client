package com.grad.pojo.vote;

import com.grad.pojo.ClientToThisInfo;
import com.grad.pojo.ImageItem;
import com.grad.pojo.Post;
import com.grad.pojo.PostInfo;
import com.grad.pojo.PostUserInfo;
import com.grad.pojo.User;

import java.util.List;

public class VoteItem {
    private Vote vote;
    private User user;
    private ClientToVoteInfo clientToThisInfo;
    private List<VoteOption> voteOptions;

    public VoteItem() {
    }

    public VoteItem(Vote vote, User user, ClientToVoteInfo clientToThisInfo, List<VoteOption> voteOptions) {
        this.vote = vote;
        this.user = user;
        this.clientToThisInfo = clientToThisInfo;
        this.voteOptions = voteOptions;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientToVoteInfo getClientToThisInfo() {
        return clientToThisInfo;
    }

    public void setClientToThisInfo(ClientToVoteInfo clientToThisInfo) {
        this.clientToThisInfo = clientToThisInfo;
    }

    public List<VoteOption> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }

    @Override
    public String toString() {
        return "VoteItem{" +
                "vote=" + vote +
                ", user=" + user +
                ", clientToThisInfo=" + clientToThisInfo +
                ", voteOptions=" + voteOptions +
                '}';
    }
}