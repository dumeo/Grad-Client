package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.information.news.CommunityNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GPCommittee {
    @POST("/committee/upload-note")
    @FormUrlEncoded
    Call<JsonObject> uploadNote(@Field("communityName")String communityName, @Field("content")String content);


    @POST("/committee/upload-news")
    Call<JsonObject> uploadNews(@Body CommunityNews communityNews);

    @POST("/committee/ban-user")
    @FormUrlEncoded
    Call<JsonObject> banUser(@Field("email")String email, @Field("days")int days);





}
