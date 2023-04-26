package com.grad.service;

import static com.grad.constants.DefaultVals.FETCH_DATA_COMPLETED;
import static com.grad.constants.DefaultVals.FETCH_DATA_FAILED;
import static com.grad.constants.DefaultVals.LOAD_MORE_DATA_COMPLETED;
import static com.grad.constants.DefaultVals.REFETCH_DATA_COMPLETED;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.constants.PostConstants;
import com.grad.http.GetPost;
import com.grad.http.GPPost;
import com.grad.pojo.CommentCntRet;
import com.grad.pojo.Post;
import com.grad.pojo.PostItem;
import com.grad.constants.DefaultVals;
import com.grad.util.JsonUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostService {

    public static void newPost(Post post, Handler handler){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GPPost GPPost = retrofit.create(GPPost.class);
        Call<JsonObject> call = GPPost.addPost(post);
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


    public static void fetchData(String postTag, Handler handler, List<PostItem> postItems, int fetchType){
        Log.e("wjj", "Fragment fetching data....");
        postItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.getPostByNewest(postTag);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    PostItem postItem = (PostItem) JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
                    postItems.add(postItem);
                }
                Message message = Message.obtain();
                if(fetchType == PostConstants.TYPE_INIT){
                    message.what = FETCH_DATA_COMPLETED;
                }else{
                    message.what = REFETCH_DATA_COMPLETED;
                }
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


    public static void loadMorePosts(String postTag, Handler handler, String startTime, List<PostItem> postItems){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.loadMorePosts(postTag, startTime);
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

    public static void getPostByid(Handler handler, String clientUid, String postId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<JsonObject> call = getPost.getPostById(clientUid, postId);
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

    public static void setLikeStatus(Handler handler, String clientUid, String postId, int transferType){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
       GPPost gpPost = retrofit.create(GPPost.class);
       Call<JsonObject> call = gpPost.setLikeStatus(clientUid, postId, transferType);
       call.enqueue(new Callback<JsonObject>() {
           @Override
           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               Message message = Message.obtain();
               message.what = DefaultVals.SET_LIKE_STATUS_SUCCESS;
               handler.sendMessage(message);
               message.obj = transferType;
           }

           @Override
           public void onFailure(Call<JsonObject> call, Throwable t) {

           }
       });
    }


}
