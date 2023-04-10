package com.grad.service;

import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonObject;
import com.grad.constants.DefaultVals;
import com.grad.constants.VoteConstants;
import com.grad.http.GPVote;
import com.grad.pojo.vote.VoteItem;

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

}
