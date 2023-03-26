package com.grad.http;

import retrofit2.Call;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPost {

    @GET("/posts")
    Call<List<JsonObject>> getPostByNewest();

}
