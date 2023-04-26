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

    @GET("/posts?")
    Call<List<JsonObject>> getPostByNewest(@Query("postTag")String postTag);
    @GET("/post/load-more?")
    Call<List<JsonObject>> loadMorePosts(@Query("postTag")String postTag, @Query("startTime") String startTime);

    @GET("/post?")
    Call<JsonObject> getPostById(@Query("clientUid")String clientUid,@Query("postId") String postId);

    @GET("/post/{postId}/comment-cnt")
    Call<JsonObject> getPostCommentCnt(@Path("postId")String postId);

}
