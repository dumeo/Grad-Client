package com.grad.service;

import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonObject;
import com.grad.http.GPCollect;
import com.grad.pojo.Status;
import com.grad.constants.DefaultVals;
import com.grad.util.JsonUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollectService {
    public static void addCollect(Handler handler, String uid, String postId, int collectType){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPCollect gpCollect = retrofit.create(GPCollect.class);
        Call<JsonObject> call = gpCollect.addCollect(uid, postId, collectType);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = JsonUtil.jsonToObject(response.body().toString(), Status.class);
                if(status.getStatus().equals(DefaultVals.STATUS_OK)){
                    Message message = Message.obtain();
                    message.what = DefaultVals.ADD_COLLECT_SUCCESS;
                    handler.sendMessage(message);
                }
                else onFailure(call, new Throwable());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = DefaultVals.ADD_COLLECT_FAILED;
                handler.sendMessage(message);
            }
        });
    }
}
