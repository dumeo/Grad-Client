package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.Post;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostNewPost {

    @POST("/posts/new")
    Call<JsonObject> addPost(@Body Post post);

}
