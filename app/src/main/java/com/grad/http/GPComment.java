package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GPComment {
    @POST("/comment/add")
    Call<JsonObject> sendComment(@Body Comment comment);

    @GET("/comment/{postId}")
    Call<List<JsonObject>> getCommentsByPostId(@Path("postId")String postId);
}
