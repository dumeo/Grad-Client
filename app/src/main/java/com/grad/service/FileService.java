package com.grad.service;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.grad.constants.DefaultVals;
import com.grad.constants.FileConstants;
import com.grad.http.GPFile;

import org.apache.commons.io.FileUtils;

import java.io.File;

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

public class FileService {

    public static void uploadFile(Handler mHandler, String filePath, int type) {
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
                mHandler.sendMessage(message);
                return;
            }
            RequestBody requestFile;
            if(type == FileConstants.IMG){
                requestFile = RequestBody.create(MediaType.parse("image/jpeg"), fileBytes);
            }else if(type == FileConstants.VIDEO){
                requestFile = RequestBody.create(MediaType.parse("video/mp4"), fileBytes);
            }else if(type == FileConstants.AUDIO){
                requestFile = RequestBody.create(MediaType.parse("audio/mp3"), fileBytes);
            }else{
                requestFile = RequestBody.create(MediaType.parse("image/jpeg"), fileBytes);
            }
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", filePath, requestFile);
            Call<JsonObject> call = gpFile.uploadFile(part);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e("wjj", "response code:" + response.code());
                    if(response.code() != HttpStatus.HTTP_OK){
                        onFailure(call, new Throwable());
                        return;
                    }
                    Message message = Message.obtain();
                    if(type == FileConstants.IMG) {
                        message.what = FileConstants.UPLOAD_IMG_OK;
                    }else if(type == FileConstants.VIDEO){
                        message.what = FileConstants.UPLOAD_VIDEO_OK;
                    }else if(type == FileConstants.AUDIO){
                        message.what = FileConstants.UPLOAD_AUDIO_OK;
                    }

                    String fileUrl= StrUtil.replace(String.valueOf(response.body().get("fileUrl")), "\"", "");
                    message.obj = fileUrl;
                    Log.e("wjj", "msg obj:" + fileUrl);
                    mHandler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Message message = Message.obtain();
                    message.what = FileConstants.UPLOAD_FILE_FAILED;
                    mHandler.sendMessage(message);
                    Log.e("wjj", "上传失败");
                }
            });
        }catch (Exception e){
                e.printStackTrace();
        }
    }



}
