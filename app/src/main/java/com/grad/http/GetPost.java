package com.grad.http;

import retrofit2.Call;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetPost {

    @GET("/posts")
    Call<List<JsonObject>> getPostByNewest();
    @GET("/post/load-more")
    Call<List<JsonObject>> loadMorePosts(@Query("startTime") String startTime);

    @GET("/post/{postId}")
    Call<JsonObject> getPostById(@Path("postId")String postId);

    @GET("/post/{postId}/comment-cnt")
    Call<JsonObject> getPostCommentCnt(@Path("postId")String postId);

}
