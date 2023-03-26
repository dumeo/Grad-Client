package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.information.addpost.ImageItem;
import com.grad.pojo.Post;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface PostNewPost {

    @POST("/posts/new")
    Call<JsonObject> addPost(@Body Post post);

    @Multipart
    @POST("/posts/new/upload-imgs")
    Call<JsonObject> addImages(@Part MultipartBody.Part images);

}
