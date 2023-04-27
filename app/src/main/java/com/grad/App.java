package com.grad;

import android.app.Application;
import android.content.Context;

import com.grad.constants.UserConstants;
import com.grad.pojo.User;
import com.grad.util.JsonUtil;
import com.grad.util.SharedPreferenceUtil;

public class App extends Application {
    private static User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        reloadUser(getApplicationContext());
    }

    public static User getUser() {
        return mUser;
    }

    public static void reloadUser(Context context){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context, UserConstants.USER_INFO_DATABASE);
        String userStr = sharedPreferenceUtil.readString(UserConstants.SHARED_PREF_USERINFO_KEY, null);
        mUser = JsonUtil.jsonToObject(userStr, User.class);
    }

    public static void storeUser(Context context, String userStr){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context, UserConstants.USER_INFO_DATABASE);
        sharedPreferenceUtil.writeString(UserConstants.SHARED_PREF_USERINFO_KEY, userStr);
        App.reloadUser(context);
    }

    public static void deleteUser(Context context){
        SharedPreferenceUtil sharedPreferenceUtil = SharedPreferenceUtil.getInstance(context, UserConstants.USER_INFO_DATABASE);
        sharedPreferenceUtil.deleteString(UserConstants.SHARED_PREF_USERINFO_KEY);
        mUser = null;
    }
}
