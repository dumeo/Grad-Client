package com.grad.service;

import static com.grad.util.DefaultVals.FETCH_DATA_COMPLETED;
import static com.grad.util.DefaultVals.FETCH_DATA_FAILED;
import static com.grad.util.DefaultVals.LOAD_MORE_DATA_COMPLETED;
import static com.grad.util.DefaultVals.REFETCH_DATA_COMPLETED;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.http.GPComment;
import com.grad.http.GetPost;
import com.grad.pojo.Comment;
import com.grad.pojo.CommentCntRet;
import com.grad.pojo.PostItem;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostService {
    public static void fetcheData(Handler handler, List<PostItem> postItems){
        postItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.getPostByNewest();
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    PostItem postItem = (PostItem) JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
                    postItems.add(postItem);
                }
                Message message = Message.obtain();
                message.what = FETCH_DATA_COMPLETED;
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = FETCH_DATA_FAILED;
                handler.sendMessage(message);
            }
        });
    }


    public static void reFetchData(Handler handler, List<PostItem> postItems){
        postItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.getPostByNewest();
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    PostItem postItem = (PostItem) JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
                    postItems.add(postItem);
                }
                Message message = Message.obtain();
                message.what = REFETCH_DATA_COMPLETED;
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Log.e("wjj", "Fetch data failed!!!!!!!!");
            }
        });
    }

    public static void loadMorePosts(Handler handler, String startTime, List<PostItem> postItems){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.loadMorePosts(startTime);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    PostItem postItem = (PostItem) JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
                    postItems.add(postItem);
                }
                Message message = Message.obtain();//for push
                message.what = LOAD_MORE_DATA_COMPLETED;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = LOAD_MORE_DATA_COMPLETED;
                handler.sendMessage(message);//333
            }
        });
    }

    public static void getPostByid(Handler handler, String postId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<JsonObject> call = getPost.getPostById(postId);
        Log.e("wjj", "call url:" + call.request().url());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                PostItem postItem = JsonUtil.jsonToObject(response.body().toString(), PostItem.class);
                Message message = Message.obtain();
                message.what = DefaultVals.GET_POST_SUCCESS;
                message.obj = postItem;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = DefaultVals.GET_POST_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void getCommentCnt(Handler handler, String postId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<JsonObject> call = getPost.getPostCommentCnt(postId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                CommentCntRet commentCntRet = JsonUtil.jsonToObject(response.body().toString(), CommentCntRet.class);
                Message message = Message.obtain();
                message.what = DefaultVals.GET_COMMENTCNT_SUCCESS;
                message.obj = commentCntRet.getCommentCnt();
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = DefaultVals.GET_COMMENTCNT_FAILED;
                handler.sendMessage(message);
            }
        });
    }


}
