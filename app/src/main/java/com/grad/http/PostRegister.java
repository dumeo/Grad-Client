package com.grad.http;

import com.google.gson.JsonObject;
import com.grad.pojo.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostRegister {
    @POST(value = "/user/register")
    Call<JsonObject> registerUser(@Body User user);
}
