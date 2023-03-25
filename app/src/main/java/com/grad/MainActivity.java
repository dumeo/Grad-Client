package com.grad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.grad.information.addpost.AddPostActivity;
import com.grad.user.RegisterActivity;
import com.grad.util.DefaultVals;
import com.grad.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String user = SharedPreferenceUtil.getInstance(getBaseContext(), DefaultVals.USER_INFO_DATABASE).readString("user", "null");
        if(user == "null")
            startActivity(new Intent(this, RegisterActivity.class));
        else startActivity(new Intent(this, AddPostActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.releaseInstance();
    }
}