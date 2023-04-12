package com.grad.pojo.vote;


public class VoteOption {

    private String optionId;
    private String voteId;
    private String optionContent;
    private long optionOrder;
    private long cnt = 0;

    public VoteOption() {
    }

    public VoteOption(String optionId, String postId, String optionContent, long optionOrder, long cnt) {
        this.optionId = optionId;
        this.voteId = postId;
        this.optionContent = optionContent;
        this.optionOrder = optionOrder;
        this.cnt = cnt;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }


    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String postId) {
        this.voteId = postId;
    }


    public String getOptionContent() {
        return optionContent;
    }

    public void setOptionContent(String optionContent) {
        this.optionContent = optionContent;
    }


    public long getOptionOrder() {
        return optionOrder;
    }

    public void setOptionOrder(long optionOrder) {
        this.optionOrder = optionOrder;
    }


    public long getCnt() {
        return cnt;
    }

    public void setCnt(long cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "VoteOption{" +
                "optionId='" + optionId + '\'' +
                ", voteId='" + voteId + '\'' +
                ", optionContent='" + optionContent + '\'' +
                ", optionOrder=" + optionOrder +
                ", cnt=" + cnt +
                '}';
    }
}