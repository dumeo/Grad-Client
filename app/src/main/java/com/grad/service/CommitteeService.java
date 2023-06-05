package com.grad.service;

import android.os.Message;

import com.google.gson.JsonObject;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.DefaultVals;
import com.grad.constants.FileConstants;
import com.grad.constants.UserConstants;
import com.grad.http.GPCommittee;
import com.grad.http.GPFile;
import com.grad.http.GPPost;
import com.grad.http.GPUser;
import com.grad.information.news.CommunityNews;
import com.grad.information.note.NoteItem;
import com.grad.information.note.NoteItemAdapter;
import com.grad.util.JsonUtil;

import android.os.Handler;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommitteeService {

    public static void uploadNote(Handler handler, String communityName, String content){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPCommittee gpCommittee = retrofit.create(GPCommittee.class);
        Call<JsonObject> call = gpCommittee.uploadNote(communityName, content);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                Message message = Message.obtain();
                message.what = CommitteeConstants.UPLOAD_NOTE_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = CommitteeConstants.UPLOAD_NOTE_FAILED;
                handler.sendMessage(message);
            }
        });
    }


    public static void uploadNews(Handler handler, CommunityNews communityNews){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPCommittee gpCommittee = retrofit.create(GPCommittee.class);
        Call<JsonObject> call = gpCommittee.uploadNews(communityNews);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                Message message = Message.obtain();
                message.what = CommitteeConstants.UPLOAD_NEWS_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = CommitteeConstants.UPLOAD_NEWS_FAILED;
                handler.sendMessage(message);
            }
        });
    }

    public static void uploadHeadImg(Handler handler, String filePath){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GPFile gpFile = retrofit.create(GPFile.class);
        File file = new File(filePath);
        Log.e("wjj", "picked file:" + filePath);
        Log.e("wjj", "tmp file name:" + file.getName());
        try{
            byte[] fileBytes = FileUtils.readFileToByteArray(file);
            int fileSize = fileBytes.length;
            Log.e("wjj", "file size = " + fileSize);
            if(fileSize > FileConstants.MAX_FILE_SIZE){
                Message message = Message.obtain();
                message.what = FileConstants.FILE_SIZE_EXCEEZED;
                handler.sendMessage(message);
                return;
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), fileBytes);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", filePath, requestFile);
            Call<JsonObject> call = gpFile.uploadFile(part);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.code() != HttpStatus.HTTP_OK){
                        onFailure(call, new Throwable());
                        return;
                    }
                    Message message = Message.obtain();
                    message.what = CommitteeConstants.UPLOAD_HEAD_IMG_OK;
                    String fileUrl= StrUtil.replace(String.valueOf(response.body().get("fileUrl")), "\"", "");
                    message.obj = fileUrl;
                    Log.e("wjj", "msg obj:" + fileUrl);
                    handler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Message message = Message.obtain();
                    message.what = FileConstants.UPLOAD_FILE_FAILED;
                    handler.sendMessage(message);
                    Log.e("wjj", "上传失败");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void banUser(Handler handler, String email, int days){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPCommittee gpCommittee = retrofit.create(GPCommittee.class);
        Call<JsonObject> call = gpCommittee.banUser(email, days);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                Message message = Message.obtain();
                message.what = CommitteeConstants.BAN_USER_OK;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Message message = Message.obtain();
                message.what = CommitteeConstants.BAN_USER_FAILED;
                handler.sendMessage(message);
            }
        });
    }



}
