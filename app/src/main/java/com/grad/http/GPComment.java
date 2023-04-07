package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GPComment {
    @POST("/comment/add")
    Call<JsonObject> sendComment(@Body Comment comment);
    @FormUrlEncoded
    @POST("/comment/like")
    Call<JsonObject> setLikeStatus(@Field("uid")String uid, @Field("commentId")String commentId, @Field("transferType")int transferType);

    @GET("/comments?")
    Call<List<JsonObject>> getCommentsByPostId(@Query("clientUid")String clientUid, @Query("postId") String postId);

    @GET("/comment/check-like?")
    Call<JsonObject> checkLikeStatus(@Query("uid")String uid, @Query("commentId")String commentId);
}
