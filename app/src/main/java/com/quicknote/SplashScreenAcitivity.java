package com.quicknote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenAcitivity extends AppCompatActivity {

    Handler mhandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenAcitivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}
