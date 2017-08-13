package com.example.sinmingu.login.Activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.sinmingu.login.R;

/**
 * Created by sinmingu on 2017-08-11.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },3000);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.not_move_activity,R.anim.activity_swap_makefog);
    }
}