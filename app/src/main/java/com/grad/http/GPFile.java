package com.grad.http;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GPFile {
    @Multipart
    @POST("/file/upload-file")
    Call<JsonObject> uploadFile(@Part MultipartBody.Part images);
}
