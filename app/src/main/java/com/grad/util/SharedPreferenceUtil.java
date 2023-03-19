package com.grad.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {
    private static SharedPreferenceUtil instance;
    private Context context;
    private SharedPreferences sharedPreferences;

    private SharedPreferenceUtil(Context context, String dataBse) {
        this.context = context.getApplicationContext();
        this.sharedPreferences = this.context.getSharedPreferences(dataBse, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferenceUtil getInstance(Context context, String dataBse) {
        if(instance == null)
            instance = new SharedPreferenceUtil(context, dataBse);
        return instance;
    }

    public void writeString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String readString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void writeInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public int readInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    private void releaseContext(){
        this.context = null;
    }

    public static synchronized void releaseInstance() {
        if(instance != null){
            instance.releaseContext();
            instance = null;
        }
    }
}
