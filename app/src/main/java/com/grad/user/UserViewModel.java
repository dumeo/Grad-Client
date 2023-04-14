package com.grad.user;

import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.grad.http.GPUser;
import com.grad.http.PostRegister;
import com.grad.pojo.User;
import com.grad.pojo.RegisterRet;
import com.grad.constants.DefaultVals;
import com.grad.util.HttpUtil;
import com.grad.util.JsonUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserViewModel{
    private ObservableField<User> userObservableField;
    private MutableLiveData<Integer> registerStatus;

    public UserViewModel(User user) {
        userObservableField = new ObservableField<>();
        registerStatus = new MutableLiveData<Integer>(DefaultVals.UNDER_REGISTER);
        userObservableField.set(user);

    }

    public User getUser(){
        return userObservableField.get();
    }

    public MutableLiveData<Integer> getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(MutableLiveData<Integer> registerStatus) {
        this.registerStatus = registerStatus;
    }


    public String getUsername(){
        return userObservableField.get().getUsername();
    }

    public void setUsername(String userName){
        userObservableField.get().setUsername(userName);
    }

    public String getPassword(){
        return userObservableField.get().getPassword();
    }

    public void setPassword(String password){
        userObservableField.get().setPassword(password);
    }

    public String getCommunityName(){
        return userObservableField.get().getCommunityName();
    }

    public void setCommunityName(String communityName){
        userObservableField.get().setCommunityName(communityName);
    }

    public String getHouseAddr(){
        return userObservableField.get().getHouseAddr();
    }

    public void setHouseAddr(String houseAddr){
        userObservableField.get().setHouseAddr(houseAddr);
    }

    public void onRegisterClicked(View view){
        if(StrUtil.isEmpty(userObservableField.get().getUsername()) ||
                StrUtil.isEmpty(userObservableField.get().getPassword())
                || StrUtil.isEmpty(userObservableField.get().getCommunityName())
                || StrUtil.isEmpty(userObservableField.get().getHouseAddr()))
            registerStatus.postValue(DefaultVals.REGISTER_UNFILL);
        else registerUser();
    }


    private void registerUser(){

        registerStatus.postValue(DefaultVals.REGISTERING);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefaultVals.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GPUser gpUser = retrofit.create(GPUser.class);
       Call<JsonObject> call = gpUser.registerUser(userObservableField.get());
       call.enqueue(new Callback<JsonObject>() {
           @Override
           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               registerStatus.postValue(DefaultVals.REGISTERING_SUCCESS);
               if(response.code() == HttpStatus.HTTP_OK){
                   User user = JsonUtil.jsonToObject(response.body().toString(), User.class);
                   userObservableField.set(user);
               }
           }

           @Override
           public void onFailure(Call<JsonObject> call, Throwable t) {
                registerStatus.postValue(DefaultVals.REGISTERING_FAILED);
           }
       });
    }

}

