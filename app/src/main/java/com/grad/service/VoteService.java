package com.grad.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.constants.DefaultVals;
import com.grad.constants.VoteConstants;
import com.grad.http.GPVote;
import com.grad.pojo.vote.VoteItem;
import com.grad.util.JsonUtil;

import java.util.List;

import cn.hutool.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VoteService {
    public static void addVote(Handler handler, VoteItem voteItem){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPVote gpVote = retrofit.create(GPVote.class);
        Call<JsonObject> call = gpVote.addVote(voteItem);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Message message = Message.obtain();
                message.what = VoteConstants.ADD_VOTE_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = VoteConstants.ADD_VOTE_FAILED;
                handler.sendMessage(message);
            }
        });

    }

    public static void fetchVotes(Handler handler, List<VoteItem> voteItems, int fetchType){
        voteItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPVote gpVote = retrofit.create(GPVote.class);
        Call<List<JsonObject>> call = gpVote.getVotesByNewest();
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.code() != HttpStatus.HTTP_OK) return;
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    VoteItem voteItem = JsonUtil.jsonToObject(jsonObject.toString(), VoteItem.class);
                    voteItems.add(voteItem);
                }
                Message message = Message.obtain();
                if(fetchType == VoteConstants.TYPE_INIT)
                    message.what = VoteConstants.FETCH_VOTES_OK;
                else if(fetchType == VoteConstants.TYPE_REFETCH)
                    message.what = VoteConstants.REFETCH_VOTES_OK;
                handler.sendMessage(message);


            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });
    }


    public static void loadMoreVotes(Handler handler, String createDate, List<VoteItem> voteItems){
        voteItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.e("wjj", "Loading more votes...");
        GPVote gpVote = retrofit.create(GPVote.class);
        Call<List<JsonObject>> call = gpVote.loadMoreVotes(createDate);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.code() != HttpStatus.HTTP_OK) return;
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    VoteItem voteItem = JsonUtil.jsonToObject(jsonObject.toString(), VoteItem.class);
                    voteItems.add(voteItem);
                }
                Message message = Message.obtain();
                message.what = VoteConstants.LOAD_MORE_VOTES_OK;
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {

            }
        });
    }

    public static void getVoteById(Handler handler, String clientUid, String voteId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPVote gpVote = retrofit.create(GPVote.class);
        Call<JsonObject> call = gpVote.getVoteById(clientUid, voteId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK) return;
                VoteItem voteItem = JsonUtil.jsonToObject(response.body().toString(), VoteItem.class);
                Message message = Message.obtain();
                message.what = VoteConstants.GET_VOTE_OK;
                message.obj = voteItem;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
