package com.grad.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.http.GPUser;
import com.grad.pojo.Status;
import com.grad.constants.DefaultVals;
import com.grad.util.JsonUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {
    public static void checkIfUserExists(Handler handler, String uid){
        Log.e("wjj", "check...");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.checkUserById(uid);
        Log.e("wjj", call.request().url().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Status status = JsonUtil.jsonToObject(response.body().toString(), Status.class);
                    Message message = Message.obtain();
                    message.what = DefaultVals.CHECK_USER_SUCCESS;
                    message.obj = status.getMsg();
                    handler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("wjj", "failed");
            }
        });
    }
}
