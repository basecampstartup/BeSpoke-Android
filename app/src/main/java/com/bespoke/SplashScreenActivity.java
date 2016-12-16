//===============================================================================
// (c) 2016 Basecamp Startups Pvt. Ltd.  All rights reserved.
// Original Author: Ankur Sharma
// Original Date: 29/11/2016
//===============================================================================
package com.bespoke;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bespoke.sprefs.AppSPrefs;

public class SplashScreenActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    /** context of current Activity */
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mContext=this;
        // Check user is login or not, if login redirect to DashBoard else to Login
        if (AppSPrefs.isAlreadyLoggedIn()) {
            goToHomeScreen();
        } else {
            goToNextScreen();
        }

    }

    /**
     * Method  to Finish Splash screen after some time.
     */
    private void goToNextScreen() {
        final int splashTime = 2000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, splashTime);
    }
    /**
     * Method  to Finish Splash screen after some time.
     */
    private void goToHomeScreen() {
        final int splashTime = 2000;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                finish();
            }
        }, splashTime);
    }




}
