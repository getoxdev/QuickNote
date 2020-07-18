package com.quicknote;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreenAcitivity extends AppCompatActivity {

    Handler mhandler;
    Animation animation;
    LottieAnimationView splashScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        splashScreen = (LottieAnimationView) findViewById(R.id.splash_screen_anim);

        //animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        splashScreen.setAnimation(animation);



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
