package com.grad.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.StateSet;

import com.google.gson.JsonObject;
import com.grad.constants.UserConstants;
import com.grad.http.GPCommittee;
import com.grad.http.GPUser;
import com.grad.information.news.CommunityNews;
import com.grad.information.note.NoteItem;
import com.grad.information.reserve.ReserveItem;
import com.grad.pojo.RegisterRet;
import com.grad.pojo.Status;
import com.grad.constants.DefaultVals;
import com.grad.pojo.User;
import com.grad.util.HttpUtil;
import com.grad.util.JsonUtil;

import java.util.List;
import java.util.Locale;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.handler.BeanListHandler;
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

    public static void getNotes(Handler handler, String communityName, List<NoteItem> noteItemList){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<List<JsonObject>> call = gpUser.getNotes(communityName);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                List<JsonObject> jsonObjects = response.body();
                for(JsonObject jsonObject : jsonObjects){
                    NoteItem noteItem = JsonUtil.jsonToObject(jsonObject.toString(), NoteItem.class);
                    noteItemList.add(noteItem);
                }
                Message message = Message.obtain();
                message.what = UserConstants.GET_NOTES_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = UserConstants.GET_NOTES_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void readNote(String noteId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.readNote(noteId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void addReserve(Handler handler, ReserveItem reserveItem){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.addReserve(reserveItem);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                Message message = Message.obtain();
                message.what = UserConstants.ADD_RESERVE_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = UserConstants.ADD_RESERVE_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void getUserReserve(Handler handler, String uid, List<ReserveItem> reserveItemList){
        reserveItemList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<List<JsonObject>> call = gpUser.getUserReserve(uid);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                List<JsonObject> jsonObjects = response.body();
                for(JsonObject jsonObject : jsonObjects){
                    ReserveItem reserveItem = JsonUtil.jsonToObject(jsonObject.toString(), ReserveItem.class);
                    reserveItemList.add(reserveItem);
                }
                Message message = Message.obtain();
                message.what = UserConstants.GET_RESERVE_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = UserConstants.GET_RESERVE_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void getNewestNote(Handler handler, String communityName){
        //构建Retrofit网路请求接口
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GPUser gpUser = retrofit.create(GPUser.class);
        //检索最新一条公告
        Call<JsonObject> call = gpUser.getNewestNote(communityName);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK || StrUtil.isEmpty(response.body().toString())){
                    onFailure(call, new Throwable());
                    return;
                }
                //得到数据进行展示
                NoteItem noteItem = JsonUtil.jsonToObject(response.body().toString(), NoteItem.class);
                Message message = Message.obtain();
                message.what = UserConstants.GET_NEWEST_NOTE_OK;
                message.obj = noteItem;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public static void getCommunityNews(Handler handler, String communityName, List<CommunityNews> communityNewsList){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<List<JsonObject>> call = gpUser.getCommunityNews(communityName);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                List<JsonObject> jsonObjects = response.body();
                for(JsonObject jsonObject : jsonObjects){
                    CommunityNews communityNews = JsonUtil.jsonToObject(jsonObject.toString(), CommunityNews.class);
                    communityNewsList.add(communityNews);
                }
                Message message = Message.obtain();
                message.what = UserConstants.GET_NEWS_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = UserConstants.GET_NEWS_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void increaseNewsViewCnt(String newsId){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.increaseNewsViewCnt(newsId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }



    public static void checkUserBanned(Handler handler, String email){
        //构建Retrofit网路请求接口
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GPUser gpUser = retrofit.create(GPUser.class);
        Call<JsonObject> call = gpUser.checkBanned(email);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, null);
                    return;
                }
                Message message = Message.obtain();
                message.what = UserConstants.CHECK_USER_BANNED_OK;
                message.obj = response.body();
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

}
