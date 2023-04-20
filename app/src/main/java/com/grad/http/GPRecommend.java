package com.grad.http;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GPRecommend {
    @GET("/recommend?")
    Call<JsonObject> getUserRecommend(@Query("uid")String uid);
}
