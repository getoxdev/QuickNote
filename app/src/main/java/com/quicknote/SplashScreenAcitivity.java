package com.quicknote;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreenAcitivity extends AppCompatActivity {

    Handler mhandler;
    Animation animation;
    Animation animation2;
    LottieAnimationView splashScreen;
    TextView appName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        splashScreen = (LottieAnimationView) findViewById(R.id.splash_screen_anim);
        appName = (TextView) findViewById(R.id.app_name);



        //animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        splashScreen.setAnimation(animation);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_card);
        appName.setAnimation(animation2);






        mhandler = new Handler();
        mhandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenAcitivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1500);

    }
}
