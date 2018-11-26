package com.edge.fintrack;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;

public class ThemeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_colorYellow, iv_colorB, iv_md_deep_orange_500, iv_colorPrimary, iv_md_green_500, iv_md_lime_600;
    RelativeLayout app_header, app_header2;

    // Session Manager Class
    SessionManager session;
    String appcolor = "#FF2C6C4B";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_theme);

        // Session class instance
        session = new SessionManager(getApplicationContext());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        try {
            // name
            String name = user.get(SessionManager.KEY_NAME);
            // email
            String email = user.get(SessionManager.KEY_EMAIL);
            appcolor = user.get(SessionManager.KEY_COLOR);
        } catch (NullPointerException e) {
            e.getMessage();
        }
        // making notification bar transparent
        changeStatusBarColor(appcolor);
        iv_colorYellow = (ImageView) findViewById(R.id.iv_colorYellow);
        iv_colorYellow.setOnClickListener(this);

        iv_colorB = (ImageView) findViewById(R.id.iv_colorB);
        iv_colorB.setOnClickListener(this);

        iv_md_deep_orange_500 = (ImageView) findViewById(R.id.iv_md_deep_orange_500);
        iv_md_deep_orange_500.setOnClickListener(this);

        iv_colorPrimary = (ImageView) findViewById(R.id.iv_colorPrimary);
        iv_colorPrimary.setOnClickListener(this);

        iv_md_green_500 = (ImageView) findViewById(R.id.iv_md_green_500);
        iv_md_green_500.setOnClickListener(this);

        iv_md_lime_600 = (ImageView) findViewById(R.id.iv_md_lime_600);
        iv_md_lime_600.setOnClickListener(this);

        app_header = (RelativeLayout) findViewById(R.id.app_header);
        app_header.setBackgroundColor(Color.parseColor(appcolor));

        app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
        app_header2.setBackgroundColor(Color.parseColor(appcolor));
    }

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor(String color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.parseColor(color));

            /*// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(getResources().getColor(R.color.colorAccent));*/

            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_colorYellow:
                app_header = (RelativeLayout) findViewById(R.id.app_header);
                app_header.setBackgroundColor(getResources().getColor(R.color.colorYellow));

                app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
                app_header2.setBackgroundColor(getResources().getColor(R.color.colorYellow));

                changeStatusBarColor(appcolor);

                session.ChangeAppColorSession("#F2A231");
                break;
            case R.id.iv_colorB:
                app_header = (RelativeLayout) findViewById(R.id.app_header);
                app_header.setBackgroundColor(getResources().getColor(R.color.colorB));

                app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
                app_header2.setBackgroundColor(getResources().getColor(R.color.colorB));

                changeStatusBarColor(appcolor);

                session.ChangeAppColorSession("#0E5393");
                break;
            case R.id.iv_md_deep_orange_500:
                app_header = (RelativeLayout) findViewById(R.id.app_header);
                app_header.setBackgroundColor(getResources().getColor(R.color.md_orange_500));

                app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
                app_header2.setBackgroundColor(getResources().getColor(R.color.md_orange_500));

                changeStatusBarColor(appcolor);
                session.ChangeAppColorSession("#FF9800");
                break;

            case R.id.iv_colorPrimary:
                app_header = (RelativeLayout) findViewById(R.id.app_header);
                app_header.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
                app_header2.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                changeStatusBarColor(appcolor);

                session.ChangeAppColorSession("#FF2C6C4B");
                break;

            case R.id.iv_md_green_500:
                app_header = (RelativeLayout) findViewById(R.id.app_header);
                app_header.setBackgroundColor(getResources().getColor(R.color.md_green_500));

                app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
                app_header2.setBackgroundColor(getResources().getColor(R.color.md_green_500));

                changeStatusBarColor(appcolor);

                session.ChangeAppColorSession("#4CAF50");
                break;

            case R.id.iv_md_lime_600:
                app_header = (RelativeLayout) findViewById(R.id.app_header);
                app_header.setBackgroundColor(getResources().getColor(R.color.md_lime_600));

                app_header2 = (RelativeLayout) findViewById(R.id.app_header2);
                app_header2.setBackgroundColor(getResources().getColor(R.color.md_lime_600));

                changeStatusBarColor(appcolor);

                session.ChangeAppColorSession("#C0CA33");
                break;
        }

    }

}
