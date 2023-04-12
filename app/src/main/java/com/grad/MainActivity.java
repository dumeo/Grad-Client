package com.grad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.grad.constants.UserConstants;
import com.grad.information.MainPageActivity;
import com.grad.information.vote.AddVoteActivity;
import com.grad.information.vote.ToVoteActivity;
import com.grad.information.vote.VoteListActivity;
import com.grad.user.RegisterActivity;
import com.grad.util.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String user = SharedPreferenceUtil.getInstance(getApplicationContext(), UserConstants.USER_INFO_DATABASE).readString(UserConstants.SHARED_PREF_USERINFO_KEY, "null");
        if(user == "null"){
            startActivity(new Intent(this, RegisterActivity.class));
        }else{
            startActivity(new Intent(this, VoteListActivity.class));
        }
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferenceUtil.releaseInstance();
    }
}