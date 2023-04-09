package com.grad.http;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GPCollect {
    @FormUrlEncoded
    @POST("/post/collect/add")
    Call<JsonObject> addCollect(@Field("uid")String clientUid, @Field("postId") String postId, @Field("collectType") int collectType);
}
