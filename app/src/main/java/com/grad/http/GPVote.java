package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.vote.VoteItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GPVote {
    @POST("/vote/add")
    Call<JsonObject> addVote(@Body VoteItem voteItem);

    @GET("/vote/newest")
    Call<List<JsonObject>> getVotesByNewest();
    @GET("/vote/more")
    Call<List<JsonObject>> loadMoreVotes(@Query("createDate")String createDate);

    @GET("/vote?")
    Call<JsonObject> getVoteById(@Query("clientUid")String clientUid, @Query("voteId")String voteId);

    @FormUrlEncoded
    @POST("/vote/to-vote")
    Call<JsonObject> vote(@Field("uid")String uid, @Field("voteId")String voteId, @Field("optionId")String optionId);

}
