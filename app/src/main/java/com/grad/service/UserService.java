package com.grad.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.StateSet;

import com.google.gson.JsonObject;
import com.grad.constants.UserConstants;
import com.grad.http.GPUser;
import com.grad.pojo.RegisterRet;
import com.grad.pojo.Status;
import com.grad.constants.DefaultVals;
import com.grad.pojo.User;
import com.grad.util.HttpUtil;
import com.grad.util.JsonUtil;

import java.util.Locale;

import cn.hutool.http.HttpStatus;
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
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK) return;
                Message message = Message.obtain();
                message.what = UserConstants.CHECK_USER_OK;
                Status status = JsonUtil.jsonToObject(response.body().toString(), Status.class);
                Log.e("wjj", "check user response:" + response.body());
                message.obj = status.getStatus();
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("wjj", "check user failed");

            }
        });
    }

    public static void registerUser(Handler handler, User user) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.registerUser(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    Message message = Message.obtain();
                    message.what = UserConstants.REGISTER_USER_FAILED;
                    message.obj = UserConstants.EMAIL_EXISTS;
                    handler.sendMessage(message);
                }else{
                    User user1 = JsonUtil.jsonToObject(response.body().toString(), User.class);
                    Message message = Message.obtain();
                    message.what = UserConstants.REGISTER_USER_OK;
                    message.obj = JsonUtil.objectToJson(user1);
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public static void loginUser(Handler handler, String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.loginUser(email, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    Message message = Message.obtain();
                    message.what = UserConstants.LOGIN_USER_FAILED;
                    handler.sendMessage(message);
                    return;
                }
                Message message = Message.obtain();
                message.what = UserConstants.LOGIN_USER_OK;
                User user = JsonUtil.jsonToObject(response.body().toString(), User.class);
                message.obj = JsonUtil.objectToJson(user);
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
