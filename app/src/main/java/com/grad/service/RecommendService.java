package com.grad.service;

import static com.grad.constants.DefaultVals.FETCH_DATA_COMPLETED;
import static com.grad.constants.DefaultVals.HTTP_CODE_OK;
import static com.grad.constants.DefaultVals.REFETCH_DATA_COMPLETED;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.constants.DefaultVals;
import com.grad.constants.PostConstants;
import com.grad.constants.RecommConstants;
import com.grad.http.GPRecommend;
import com.grad.pojo.PostItem;
import com.grad.util.JsonUtil;

import java.util.List;

import cn.hutool.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendService {

    public static void getRecommend(String uid, Handler handler, List<PostItem> postItems, int fetchType){
        Log.e("wjj", "Getting Recomms, fetchType = " + fetchType);
        postItems.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GPRecommend gpRecommend = retrofit.create(GPRecommend.class);
        Call<List<JsonObject>> call = gpRecommend.getRecommend(uid);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if(response.code() != HttpStatus.HTTP_OK){
                    onFailure(call, new Throwable());
                    return;
                }
                List<JsonObject> res = response.body();
                for(JsonObject jsonObject : res){
                    PostItem postItem = JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
                    postItems.add(postItem);
                }
                Message message = Message.obtain();
                if(fetchType == RecommConstants.FETCH){
                    message.what = RecommConstants.FETCH_OK;
                }else if(fetchType == RecommConstants.REFETCH){
                    message.what = RecommConstants.REFETCH_OK;
                }else if(fetchType == RecommConstants.LOAD_MORE){
                    message.what = RecommConstants.LOAD_MORE_OK;
                }
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable t) {
                Message message = Message.obtain();
                message.what = RecommConstants.FETCH_FAILED;
                handler.sendMessage(message);
            }
        });
    }

}
