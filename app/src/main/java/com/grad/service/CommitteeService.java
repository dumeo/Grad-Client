package com.grad.service;

import android.os.Message;

import com.google.gson.JsonObject;
import com.grad.constants.CommitteeConstants;
import com.grad.constants.DefaultVals;
import com.grad.constants.UserConstants;
import com.grad.http.GPCommittee;
import com.grad.http.GPUser;
import com.grad.information.note.NoteItem;
import com.grad.information.note.NoteItemAdapter;
import com.grad.util.JsonUtil;

import android.os.Handler;

import java.util.List;

import cn.hutool.http.HttpStatus;
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






}
