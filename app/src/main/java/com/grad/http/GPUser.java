package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.information.reserve.ReserveItem;
import com.grad.pojo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GPUser {

    @POST(value = "/user/register")
    Call<JsonObject> registerUser(@Body User user);

    @GET("/user/check?")
    Call<JsonObject> checkUserById(@Query("uid")String uid);

    @FormUrlEncoded
    @POST("/user/login")
    Call<JsonObject> loginUser(@Field("email")String email, @Field("password")String psw);

    @GET("/user/notes?")
    Call<List<JsonObject>> getNotes(@Query("communityName")String communityName);


    @POST("/user/read-note")
    @FormUrlEncoded
    Call<JsonObject> readNote(@Field("noteId") String noteId);

    @POST("/user/add-reserve")
    Call<JsonObject> addReserve(@Body ReserveItem reserveItem);

    @GET("/user/reserve?")
    Call<List<JsonObject>> getUserReserve(@Query("uid")String uid);

    @GET("/user/get-newest-note")
    Call<JsonObject> getNewestNote(@Query("communityName")String communityName);

}
