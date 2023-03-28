package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.Post;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PostNewPost {

    @POST("/posts/new")
    Call<JsonObject> addPost(@Body Post post);

    @Multipart
    @POST("/posts/new/upload-imgs")
    Call<JsonObject> addImages(@Part MultipartBody.Part images);

}
