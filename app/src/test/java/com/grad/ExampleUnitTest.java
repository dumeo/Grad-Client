package com.grad;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.google.gson.JsonObject;
import com.grad.http.GetPost;
import com.grad.pojo.PostItem;
import com.grad.util.DefaultVals;
import com.grad.util.JsonUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getPostsTest() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetPost getPost = retrofit.create(GetPost.class);
        Call<List<JsonObject>> call = getPost.getPostByNewest("post_date", 0);

        Response<List<JsonObject>> res = call.execute();
        for(JsonObject jsonObject : res.body()){
            PostItem postItem = (PostItem) JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
            System.out.println("postItem:" + postItem);
        }
    }
}