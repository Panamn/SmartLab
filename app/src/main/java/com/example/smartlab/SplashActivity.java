package com.example.smartlab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 3000;
    private ASession aSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        aSession = new ASession(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!aSession.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, Onboard1Activity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, PinActivity.class));
                    finish();
                }
            }
        }, SPLASH_DELAY);
    }
}
