package com.edge.fintrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    final String welcomeScreenShownPref = "welcomeScreenShown";
    SessionManager session;
    // All Shared Preferences Keys
    SharedPreferences mPrefs;
    Boolean welcomeScreenShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);
        // making notification bar transparent
        changeStatusBarColor();
        // Session Manager
        session = new SessionManager(getApplicationContext());

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                /*if (!welcomeScreenShown) {
                    // here you can launch another activity if you like
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putBoolean(welcomeScreenShownPref, true);
                    editor.commit(); // Very important to save the preference
                    Intent mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SplashActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    SplashActivity.this.finish();
                } else {
                    if (!session.isLoggedIn()) {
                        Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SplashActivity.this.startActivity(mainIntent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        SplashActivity.this.finish();
                        //Toast.makeText(SplashActivity.this, "if isLoggedIn ", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SplashActivity.this.startActivity(mainIntent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                        SplashActivity.this.finish();
                        //Toast.makeText(SplashActivity.this, "else isLoggedIn ", Toast.LENGTH_SHORT).show();
                    }
                }*/
                if (!session.isLoggedIn()) {
                    Intent mainIntent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SplashActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    SplashActivity.this.finish();
                    //Toast.makeText(SplashActivity.this, "if isLoggedIn ", Toast.LENGTH_SHORT).show();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SplashActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                    SplashActivity.this.finish();
                    //Toast.makeText(SplashActivity.this, "else isLoggedIn ", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1500); //
        // Duration of wait+++++++++++++
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

}
