package com.grad.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.constants.DefaultVals;
import com.grad.constants.FileConstants;
import com.grad.http.GPFile;

import org.apache.commons.io.FileUtils;

import java.io.File;

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
        Log.e("wjj", "file name:" + file.getName());
        try{
            byte[] fileBytes = FileUtils.readFileToByteArray(file);
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
                    if(type == FileConstants.IMG) {
                        message.what = FileConstants.UPLOAD_IMG_OK;
                    }else if(type == FileConstants.VIDEO){
                        message.what = FileConstants.UPLOAD_VIDEO_OK;
                    }

                    message.obj = response.body().get("fileUrl");
                    Log.e("wjj", response.body().get("fileUrl").toString());
                    mHandler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Message message = Message.obtain();
                    message.what = FileConstants.UPLOAD_IMG_FAILED;
                    mHandler.sendMessage(message);
                    Log.e("wjj", "上传失败");
                }
            });
        }catch (Exception e){
                e.printStackTrace();
        }
    }
}
