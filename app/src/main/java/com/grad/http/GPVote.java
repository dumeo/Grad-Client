package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.vote.VoteItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GPVote {
    @POST("/vote/add")
    Call<JsonObject> addVote(@Body VoteItem voteItem);
}
