package com.grad.pojo.vote;

public class ClientToVoteInfo {
    private boolean isVoted;
    private String optionId;

    public ClientToVoteInfo() {
    }

    public ClientToVoteInfo(boolean isVoted, String optionId) {
        this.isVoted = isVoted;
        this.optionId = optionId;
    }

    public boolean isVoted() {
        return isVoted;
    }

    public void setVoted(boolean voted) {
        isVoted = voted;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    @Override
    public String toString() {
        return "ClientToVoteInfo{" +
                "isVoted=" + isVoted +
                ", optionId='" + optionId + '\'' +
                '}';
    }
}
