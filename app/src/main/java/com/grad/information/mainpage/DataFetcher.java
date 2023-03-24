package com.grad.information.mainpage;

import static com.grad.util.DefaultVals.FETCH_DATA_COMPLETED;
import static com.grad.util.DefaultVals.REFETCH_DATA_COMPLETED;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.http.GetPost;
import com.grad.pojo.PostItem;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFetcher {
    public static void fetcheData(Handler handler, List<PostItem> postItems){
        postItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.getPostByNewest(0,"newest");
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
                Log.e("wjj", "Fetch data failed!!!!!!!!");
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
        Call<List<JsonObject>> call = getPost.getPostByNewest(0,"newest");
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
}