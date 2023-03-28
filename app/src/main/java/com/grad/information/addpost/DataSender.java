package com.grad.information.addpost;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.http.PostNewPost;
import com.grad.pojo.Post;
import com.grad.pojo.User;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataSender {

    public static void newPost(Post post, Handler handler){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostNewPost postNewPost = retrofit.create(PostNewPost.class);
        Call<JsonObject> call = postNewPost.addPost(post);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                String postId = jsonObject.get("postId").getAsString();
                Message message = Message.obtain();
                message.what = DefaultVals.ADD_POST_TEXT_SUCCESS;
                message.obj = postId;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public static void sendImages(String postId, Handler mHandler1, ImageInfo imageInfo, int pos) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Bitmap bitmap = imageInfo.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        String fileName = postId + ":" + pos + ":" + imageInfo.getFileName();
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", fileName, requestFile);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostNewPost postNewPost = retrofit.create(PostNewPost.class);
        Call<JsonObject> call = postNewPost.addImages(part);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("wjj", "请求成功:" + pos);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("wjj", "请求失败:" + pos);
            }
        });


    }


}
