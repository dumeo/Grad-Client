package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.Post;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GPPost {

    @POST("/post/new")
    Call<JsonObject> addPost(@Body Post post);


    @Multipart
    @POST("/post/new/upload-imgs")
    Call<JsonObject> addImages(@Part MultipartBody.Part images);

    @FormUrlEncoded
    @POST("/post/like")
    Call<JsonObject> setLikeStatus(@Field("uid")String uid, @Field("postId")String postId, @Field("transferType")int transferType);


}
