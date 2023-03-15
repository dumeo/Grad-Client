package com.grad.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    public static <T> String objectToJson(T object){
        Gson gson = new Gson();
        String res = gson.toJson(object);
        return res;
    }

    public  static <T> T jsonToObject(String json, Class<T> classOfT) throws JsonSyntaxException {
        Gson gson = new Gson();
        T object = gson.fromJson(json, TypeToken.get(classOfT));
        return Primitives.wrap(classOfT).cast(object);
    }

}
