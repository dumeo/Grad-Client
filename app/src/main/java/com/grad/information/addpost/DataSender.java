package com.grad.information.addpost;

import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonObject;
import com.grad.http.PostNewPost;
import com.grad.pojo.Post;
import com.grad.util.DefaultVals;


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



}
