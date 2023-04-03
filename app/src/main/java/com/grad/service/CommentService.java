package com.grad.service;

import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonObject;
import com.grad.http.GPComment;
import com.grad.pojo.Comment;
import com.grad.pojo.CommentItem;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentService {

    public static void sendComment(Handler handler, Comment comment){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPComment gpComment = retrofit.create(GPComment.class);
        Call<JsonObject> call = gpComment.sendComment(comment);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Comment comment1 = (Comment) JsonUtil.jsonToObject(response.body().toString(), Comment.class);
                if(comment1.getCommentId() == null) onFailure(call, new Throwable());
                else{
                    Message message = Message.obtain();
                    message.what = DefaultVals.ADD_COMMENT_SUCCESS;
                    message.obj = comment1;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = DefaultVals.ADD_COMMENT_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void getCommentsByPostId(Handler handler, String postId, List<CommentItem> commentItems){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPComment gpComment = retrofit.create(GPComment.class);
        Call<List<JsonObject>> call = gpComment.getCommentsByPostId(postId);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                List<JsonObject> jsonObjects = response.body();
                for(JsonObject jsonObject : jsonObjects){
                    commentItems.add(JsonUtil.jsonToObject(jsonObject.toString(), CommentItem.class));
                }
                Message message = Message.obtain();
                message.what = DefaultVals.LOAD_COMMENTS_SUCCESS;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = DefaultVals.LOAD_COMMENTS_FAILED;
                handler.sendMessage(message);
            }
        });
    }



}
