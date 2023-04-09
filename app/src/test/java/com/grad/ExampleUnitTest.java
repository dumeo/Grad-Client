package com.grad;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

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
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://localhost:8080/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        GetPost getPost = retrofit.create(GetPost.class);
//        Call<List<JsonObject>> call = getPost.getPostByNewest("newest");
//
//        Response<List<JsonObject>> res = call.execute();
//        for(JsonObject jsonObject : res.body()){
//            PostItem postItem = (PostItem) JsonUtil.jsonToObject(jsonObject.toString(), PostItem.class);
//            System.out.println("postItem:" + postItem);
//        }
    }
}