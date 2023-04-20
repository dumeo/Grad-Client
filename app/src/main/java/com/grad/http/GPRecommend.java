package com.grad.http;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GPRecommend {
    @GET("/recommend?")
    Call<List<JsonObject>> getRecommend(@Query("uid")String uid);
}
