package com.grad.pojo.vote;

import com.grad.pojo.User;

import java.util.List;

public class VoteItem {
    private Vote vote;
    private User user;
    private ClientToVoteInfo clientToVoteInfo;
    private List<VoteOption> voteOptions;

    public VoteItem() {
    }

    public VoteItem(Vote vote, User user, ClientToVoteInfo clientToVoteInfo, List<VoteOption> voteOptions) {
        this.vote = vote;
        this.user = user;
        this.clientToVoteInfo = clientToVoteInfo;
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

    public ClientToVoteInfo getClientToVoteInfo() {
        return clientToVoteInfo;
    }

    public void setClientToVoteInfo(ClientToVoteInfo clientToVoteInfo) {
        this.clientToVoteInfo = clientToVoteInfo;
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
                ", clientToThisInfo=" + clientToVoteInfo +
                ", voteOptions=" + voteOptions +
                '}';
    }
}