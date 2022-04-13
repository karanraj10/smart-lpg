package com.example.lpgcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Intent intent;
    public static SharedpreferencesHandler sharedpreferencesHandler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedpreferencesHandler = new SharedpreferencesHandler(SplashActivity.this);

        // Method for Delay in Splash Activity..
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (sharedpreferencesHandler.isLoggedIn())
                {
                    updateUI(HomeActivity.class);
                }
                else
                {
                    updateUI(MainActivity.class);
                }
            }
        },Long.parseLong(getApplicationContext().getString(R.string.delay_for_splash_screen)));
    }

    private void updateUI(Class cls)
    {
        intent = new Intent(SplashActivity.this,cls);
        startActivity(intent);
        finish();
    }
}