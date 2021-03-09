package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        userSession=new UserSession(getApplicationContext());

        System.out.println(userSession.getUserDetails().toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(!userSession.isUserLoggedIn()) {
                 i = new Intent(SplashScreen.this,
                         MainActivity.class);
                 //Intent is used to switch from one activity to another.
             }else {
                    i = new Intent(SplashScreen.this,Home.class);
                }
                startActivity(i);
                //invoke the SecondActivity.

                finish();
                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}